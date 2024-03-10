package com.example.mybankapplication.service.auth;

import com.example.mybankapplication.dao.entities.UserEntity;
import com.example.mybankapplication.dao.repository.UserRepository;
import com.example.mybankapplication.enumeration.auth.Role;
import com.example.mybankapplication.exception.DuplicateDataException;
import com.example.mybankapplication.exception.NotFoundException;
import com.example.mybankapplication.model.auth.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @Transactional
    public AuthenticationResponseDto register(RegisterRequest request) {
        log.info("Registering user with email: {}", request.getEmail());

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateDataException("User with email address already exists: " + request.getEmail());
        }

        UserEntity user = UserEntity.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .birthDate(request.getBirthDate())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .phoneNumber(request.getPhoneNumber())
                .role(request.getRoles().isEmpty() ? Role.USER : request.getRoles().iterator().next())
                .build();

        userRepository.save(user);

        String jwtToken = jwtService.generateToken(user);
        log.info("User registered successfully: {}", request.getEmail());

        return AuthenticationResponseDto.builder()
                .token(jwtToken)
                .build();
    }

    @Transactional
    public AuthenticationResponseDto authenticate(AuthenticationRequest request) {
        log.info("Authenticating user: {}", request.getEmail());

        UserEntity user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new NotFoundException("User with email " + request.getEmail() + " not found"));

        try {

            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );

            var jwtToken = jwtService.generateToken(user);
            log.info("User authenticated successfully: {}", request.getEmail());
            return AuthenticationResponseDto.builder().token(jwtToken).build();
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Invalid username or password");
        }
    }
}

