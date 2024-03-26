package org.matrix.izumbankapp.service.impl;

import org.matrix.izumbankapp.dao.entities.AccountEntity;
import org.matrix.izumbankapp.dao.entities.UserEntity;
import org.matrix.izumbankapp.enumeration.auth.Role;
import org.matrix.izumbankapp.exception.*;
import org.matrix.izumbankapp.mapper.UserMapper;
import org.matrix.izumbankapp.model.accounts.AccountResponse;
import org.matrix.izumbankapp.model.auth.ResponseDto;
import org.matrix.izumbankapp.model.users.*;
import org.matrix.izumbankapp.dao.repository.UserRepository;
import org.matrix.izumbankapp.model.users.profile.UserProfileDto;
import org.matrix.izumbankapp.model.users.profile.UserProfileFilterDto;
import org.matrix.izumbankapp.service.AccountService;
import org.matrix.izumbankapp.service.UserProfileService;
import org.matrix.izumbankapp.service.UserService;
import org.matrix.izumbankapp.util.GenerateRandom;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final UserProfileService userProfileService;
    private final UserMapper userMapper;

    private static final String NOT_FOUND_WITH_ID = "User not found with ID: ";

    @Override
    public Page<UserProfileDto> findUsersByFilter(UserProfileFilterDto filter, Pageable pageRequest) {
        log.info("Searching users by filter: {}", filter);
        return userProfileService.findUsersProfileByFilter(filter, pageRequest);
    }

    @Override
    public List<UserResponse> getAllUser() {
        log.info("Retrieving all users");
        var userResponse = userRepository.findAll().stream().map(userMapper::toDto).toList();
        log.info("Successfully retrieved all users");
        return userResponse;
    }

    @Override
    public UserResponse getUserById(Long id) {
        log.info("Retrieving user by ID: {}", id);
        UserEntity userEntity = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_WITH_ID + id));
        UserResponse userResponse = userMapper.toDto(userEntity);
        log.info("Successfully retrieved User with ID: {}", id);
        return userResponse;
    }

    @Override
    public UserAccountsResponse getUserByIdForAccount(Long id) {
        log.info("Retrieving user by ID: {}", id);
        var userEntity = userRepository.findById(id).orElseThrow(() -> new NotFoundException(NOT_FOUND_WITH_ID + id));
        UserAccountsResponse userResponse = userMapper.toAccountsDto(userEntity);
        log.info("Successfully retrieved User with ID: {}", id);
        return userResponse;
    }

    @Override
    public UserResponse getUserByEmail(String email) {
        log.info("Retrieving user by email: {}", email);
        var userEntity = userRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("User not found with email: " + email));
        UserResponse userResponse = userMapper.toDto(userEntity);
        log.info("Successfully retrieved user with email: {}", email);
        return userResponse;
    }

    @Override
    public ResponseDto updateUser(Long id, UserUpdateDto userUpdateDto) {
        log.info("Updating user with ID {} to: {}", id, userUpdateDto);
        UserEntity userEntity = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_WITH_ID + id));
        userEntity = userMapper.updateEntityFromRequest(userUpdateDto, userEntity);
        userRepository.save(userEntity);
        log.info("Successfully updated user with ID: {}", id);
        return ResponseDto.builder().responseMessage("User updated successfully").build();
    }

    @Override
    public ResponseDto deleteUserById(Long id) {
        log.info("Deleting user by ID: {}", id);
        var user = userRepository.findById(id).orElseThrow(() -> new NotFoundException(NOT_FOUND_WITH_ID + id));
        userRepository.deleteById(id);
        userProfileService.deleteUserProfileById(user.getUserProfile().getUserProfileId());
        log.info("Successfully deleted user with ID: {}", id);
        return ResponseDto.builder().responseMessage("User deleted successfully").build();
    }

    @Override
    public ResponseDto addUser(UserCreateDto userCreateDto) {
        log.info("Adding new user: {}", userCreateDto);
        validateNewUserData(userCreateDto);
        UserEntity user = userMapper.toEntity(userCreateDto);
        user.setPassword(passwordEncoder.encode(userCreateDto.getPassword()));
        user.setRole(userCreateDto.getRoles().isEmpty() ? Role.USER : userCreateDto.getRoles().iterator().next());
        try {
            userRepository.save(user);
            log.info("Successfully added new user");
            return ResponseDto.builder()
                    .responseMessage("User created successfully").build();
        } catch (DataAccessException ex) {
            throw new DatabaseException("Failed to add new user to the database", ex);
        }
    }

    @Override
    public void createCif(Long userId) {
        log.info("Creating cif for user with ID: {}", userId);
        var user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException(NOT_FOUND_WITH_ID + userId));
        user.setCif(GenerateRandom.generateCif());
        userRepository.save(user);
        log.info("Successfully generate cif for user with ID: {}", user);
    }

    //to-do: Cache system
    private synchronized void validateNewUserData(UserCreateDto userCreateDto) {
        Optional<UserEntity> existingUserByEmail = userRepository.findByEmail(userCreateDto.getEmail());
        if (existingUserByEmail.isPresent())
            throw new DuplicateDataException("User with email " + userCreateDto.getEmail() + " already exists");

        if (userProfileService.existingUserprofileByPhoneNumber(userCreateDto.getUserProfile().getPhoneNumber()))
            throw new DuplicateDataException("User with phone number " + userCreateDto.getUserProfile().getPhoneNumber() + " already exists");
    }

}
