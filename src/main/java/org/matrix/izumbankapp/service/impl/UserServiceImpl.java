package org.matrix.izumbankapp.service.impl;

import org.matrix.izumbankapp.dao.entities.User;
import org.matrix.izumbankapp.enumeration.auth.Role;
import org.matrix.izumbankapp.exception.*;
import org.matrix.izumbankapp.mapper.UserMapper;
import org.matrix.izumbankapp.model.users.*;
import org.matrix.izumbankapp.dao.repository.UserRepository;
import org.matrix.izumbankapp.model.users.profile.UserProfileDto;
import org.matrix.izumbankapp.model.users.profile.UserProfileFilterDto;
import org.matrix.izumbankapp.service.UserProfileService;
import org.matrix.izumbankapp.service.UserService;
import org.matrix.izumbankapp.util.GenerateRandom;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final UserProfileService userProfileService;
    private final UserMapper userMapper;

    private static final String NOT_FOUND_WITH_ID = "User with ID %d not found";

    @Override
    public Page<UserProfileDto> findByFilter(UserProfileFilterDto filter, Pageable pageRequest) {
        log.info("Searching users by filter: {}", filter);
        return userProfileService.findByFilter(filter, pageRequest);
    }

    @Override
    public UserResponse getById(Long id) {
        log.info("Retrieving user by ID: {}", id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format(NOT_FOUND_WITH_ID, id)));
        UserResponse userResponse = userMapper.toDto(user);
        log.info("Successfully retrieved User with ID: {}", id);
        return userResponse;
    }


    public UserAccountsResponse getByAccountNumber(String accountNumber) {
        log.info("Reading user by account number {}", accountNumber);

        var userAccountsDto = userRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new NotFoundException("Account not fount with number " + accountNumber));
        log.info("Read user by account number {} successfully", accountNumber);
        return userMapper.toAccountsDto(userAccountsDto);
    }

    @Override
    public UserResponse getByEmail(String email) {
        log.info("Retrieving user by email: {}", email);
        var userEntity = userRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("User not found with email: " + email));
        UserResponse userResponse = userMapper.toDto(userEntity);
        log.info("Successfully retrieved user with email: {}", email);
        return userResponse;
    }

    @Override
    public UserResponse update(Long id, UserUpdateDto userUpdateDto) {
        log.info("Updating user with ID {} to: {}", id, userUpdateDto);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format(NOT_FOUND_WITH_ID, id)));
        user = userMapper.updateEntityFromRequest(userUpdateDto, user);
        userRepository.save(user);
        log.info("Successfully updated user with ID: {}", id);
        return userMapper.toDto(user);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        log.info("Deleting user by ID: {}", id);
        userRepository.deleteById(id);
        log.info("Successfully deleted user with ID: {}", id);
    }


    @Override
    public UserResponse create(UserCreateDto userCreateDto) {
        log.info("Adding new user: {}", userCreateDto);
        validateNewUserData(userCreateDto);
        User user = userMapper.toEntity(userCreateDto);
        user.setPassword(passwordEncoder.encode(userCreateDto.getPassword()));
        user.setRole(userCreateDto.getRoles().isEmpty() ? Role.USER : userCreateDto.getRoles().iterator().next());
        userRepository.save(user);
        log.info("Successfully added new user");
        return userMapper.toDto(user);
    }

    @Override
    public void createCif(Long userId) {
        log.info("Creating cif for user with ID: {}", userId);
        var user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException(String.format(NOT_FOUND_WITH_ID, userId)));
        if (user.getCif() == null) {
            user.setCif(GenerateRandom.generateCif());
            userRepository.save(user);

            log.info("Successfully generate cif for user with ID: {}", user);
        } else log.info("The user has CIF");

    }

    private void validateNewUserData(UserCreateDto userCreateDto) {
        Optional<User> existingUserByEmail = userRepository.findByEmail(userCreateDto.getEmail());
        if (existingUserByEmail.isPresent())
            throw new DuplicateDataException("User with email " + userCreateDto.getEmail() + " already exists");

        if (userProfileService.existsByPhoneNumber(userCreateDto.getUserProfile().getPhoneNumber()))
            throw new DuplicateDataException("User with phone number " + userCreateDto.getUserProfile().getPhoneNumber() + " already exists");
    }

}
