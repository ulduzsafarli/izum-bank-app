package com.example.mybankapplication.service.auth;

import com.example.mybankapplication.dao.entities.PassportEntity;
import com.example.mybankapplication.dao.entities.UserEntity;
import com.example.mybankapplication.dao.repository.PassportRepository;
import com.example.mybankapplication.dao.repository.UserRepository;
import com.example.mybankapplication.enumeration.auth.Role;
import com.example.mybankapplication.exception.DuplicateDataException;
import com.example.mybankapplication.exception.NotFoundException;
import com.example.mybankapplication.mapper.PassportMapper;
import com.example.mybankapplication.model.PassportDto;
import com.example.mybankapplication.model.auth.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PassportRepository passportRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final PassportMapper passportMapper;

    public AuthenticationResponseDto register(RegisterRequest request) {
        log.info("Registering user with fin and email: {}, {}", request.getFin(), request.getEmail());

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateDataException("User with email address already exists: " + request.getEmail());
        }

        PassportDto passportDto = findValidPassport(request.getFin());

        UserEntity user = UserEntity.builder()
                .firstName(passportDto.getName())
                .lastName(passportDto.getSurname())
                .birthDate(passportDto.getBirthDate())
                .fin(request.getFin())
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

    public AuthenticationResponseDto authenticate(AuthenticationRequest request) {
        log.info("Authenticating user: {}", request.getEmail());

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        UserEntity user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new NotFoundException("User with email " + request.getEmail() + " not found"));

        String jwtToken = jwtService.generateToken(user);
        log.info("User authenticated successfully: {}", request.getEmail());

        return AuthenticationResponseDto.builder()
                .token(jwtToken)
                .build();
    }

    private PassportDto findValidPassport(String fin) {
        PassportEntity passport = passportRepository.findByPersonalNo(fin)
                .orElseThrow(() -> new NotFoundException("Passport with personal number " + fin + " not found"));

        if (passport.getExpiredDate().isBefore(LocalDate.now())) {
            throw new NotFoundException("Passport with personal number " + fin + " expired");
        }

        return passportMapper.mapToDto(passport);
    }
}
