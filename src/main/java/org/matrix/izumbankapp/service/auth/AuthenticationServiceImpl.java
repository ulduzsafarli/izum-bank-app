package org.matrix.izumbankapp.service.auth;

import org.matrix.izumbankapp.dao.entities.User;
import org.matrix.izumbankapp.dao.entities.UserProfile;
import org.matrix.izumbankapp.dao.repository.UserRepository;
import org.matrix.izumbankapp.exception.DuplicateDataException;
import org.matrix.izumbankapp.exception.NotFoundException;
import org.matrix.izumbankapp.model.auth.*;
import org.matrix.izumbankapp.enumeration.auth.Role;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.matrix.izumbankapp.service.AuthenticationService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationServiceImpl implements AuthenticationService {

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

        UserProfile userProfile = UserProfile.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .birthDate(request.getBirthDate())
                .phoneNumber(request.getPhoneNumber())
                .gender(request.getGender())
                .nationality(request.getNationality())
                .build();

        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .cif(null)
                .role(request.getRoles().isEmpty() ? Role.USER : request.getRoles().iterator().next())
                .userProfile(userProfile)
                .build();

        userRepository.save(user);

        String jwtToken = jwtService.generateToken(user);
        log.info("User registered successfully: {}", request.getEmail());

        return new AuthenticationResponseDto(jwtToken);
    }

    @Transactional
    public AuthenticationResponseDto authenticate(AuthenticationRequest request) {
        log.info("Authenticating user: {}", request.email());

        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new NotFoundException("User with email " + request.email() + " not found"));
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.email(),
                            request.password()
                    )
            );

            String jwtToken = jwtService.generateToken(user);
            log.info("User authenticated successfully: {}", request.email());
            return new AuthenticationResponseDto(jwtToken);
    }

    public void changePassword(ChangePasswordRequest request, Principal connectedUser) {
        log.info("Changing the password for user: {}", connectedUser.getName());

        User user = userRepository.findByEmail(connectedUser.getName())
                .orElseThrow(() -> new NotFoundException("User with email " + connectedUser.getName() + " not found"));

        if (!passwordEncoder.matches(request.currentPassword(), user.getPassword())) {
            throw new IllegalStateException("Wrong password");
        }
        if (!request.newPassword().equals(request.confirmationPassword())) {
            throw new IllegalStateException("Passwords are not the same");
        }
        if (passwordEncoder.matches(request.newPassword(), user.getPassword())){
            throw new IllegalStateException("The same passwords");
        }
        user.setPassword(passwordEncoder.encode(request.newPassword()));
        userRepository.save(user);
        log.info("Changed the password for user: {} successfully", user.getEmail());
    }
}
