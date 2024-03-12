package com.example.mybankapplication.service.impl;

import com.example.mybankapplication.dao.entities.UserEntity;
import com.example.mybankapplication.dao.entities.UserProfileEntity;
import com.example.mybankapplication.dao.repository.UserProfileRepository;
import com.example.mybankapplication.enumeration.auth.Role;
import com.example.mybankapplication.exception.*;
import com.example.mybankapplication.mapper.UserMapper;
import com.example.mybankapplication.mapper.UserProfileMapper;
import com.example.mybankapplication.model.auth.ResponseDto;
import com.example.mybankapplication.model.users.*;
import com.example.mybankapplication.dao.repository.UserRepository;
import com.example.mybankapplication.model.users.profile.UserProfileDto;
import com.example.mybankapplication.model.users.profile.UserProfileFilterDto;
import com.example.mybankapplication.service.AccountService;
import com.example.mybankapplication.service.UserService;
import com.example.mybankapplication.specifications.UserProfileSpecifications;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
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
    private final UserProfileRepository userProfileRepository;
    private final AccountService accountService;
    private final UserMapper userMapper;
    private final UserProfileMapper userProfileMapper;

    @Value("200")
    private String responseCodeSuccess;

    @Value("204")
    private String responseCodeNoContent;

    @Override
    public Page<UserProfileDto> findUsersByFilter(UserProfileFilterDto filterDto, Pageable pageRequest) {
        log.info("Searching users by filter: {}", filterDto);
        Specification<UserProfileEntity> userProfileSpecification = UserProfileSpecifications.getUserProfileSpecification(filterDto);
        Page<UserProfileEntity> userProfileEntity = userProfileRepository.findAll(userProfileSpecification, pageRequest);
        log.info("Successfully found users");
        return userProfileEntity.map(userProfileMapper::toDto);
    }

    @Override
    public List<UserResponse> getAllUser() {
        log.info("Retrieving all users");
        List<UserResponse> userRespons = userRepository.findAll().stream()
                .map(userMapper::toDto).toList();
        log.info("Successfully retrieved all users");
        return userRespons;
    }

    @Override
    public UserResponse readUserById(Long id) {
        log.info("Retrieving user by ID: {}", id);
        UserEntity userEntity = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found with ID: " + id));
        UserResponse userResponse = userMapper.toDto(userEntity);
        log.info("Successfully retrieved User with ID: {}", id);
        return userResponse;
    }

    @Override
    public UserResponse readUserByEmail(String email) {
        log.info("Retrieving user by email: {}", email);
        UserEntity userEntity = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User not found with email: " + email));
        UserResponse userResponse = userMapper.toDto(userEntity);
        log.info("Successfully retrieved user with email: {}", email);
        return userResponse;
    }

    @Override
    public ResponseDto updateUser(Long id, UserRequestDto userRequestDto) {
        log.info("Updating user with ID {} to: {}", id, userRequestDto);
        UserEntity userEntity = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found with ID: " + id));
        userEntity = userMapper.updateEntityFromRequest(userRequestDto, userEntity);
        userRepository.save(userEntity);
        log.info("Successfully updated user with ID: {}", id);
        return ResponseDto.builder()
                .responseMessage("User created successfully")
                .responseCode(responseCodeSuccess).build();
    }

    @Override
    public ResponseDto deleteUserById(Long id) {
        log.info("Deleting user by ID: {}", id);
        if (!userRepository.existsById(id))
            throw new NotFoundException("User not found with ID: " + id);
        userRepository.deleteById(id);
        log.info("Successfully deleted user with ID: {}", id);
        return ResponseDto.builder()
                .responseMessage("User deleted successfully")
                .responseCode(responseCodeNoContent).build();
    }

    @Override
    public ResponseDto addUser(UserRequestDto userRequestDto) {
        log.info("Adding new user: {}", userRequestDto);
        validateNewUserData(userRequestDto);
        UserEntity user = userMapper.toEntity(userRequestDto);
        user.setPassword(passwordEncoder.encode(userRequestDto.getPassword()));
        user.setRole(userRequestDto.getRoles().isEmpty() ? Role.USER : userRequestDto.getRoles().iterator().next());
        try {
            userRepository.save(user);
            log.info("Successfully added new user");
            return ResponseDto.builder()
                    .responseMessage("User created successfully")
                    .responseCode(responseCodeSuccess).build();
        } catch (DataAccessException ex) {
            throw new DatabaseException("Failed to add new user to the database", ex);
        }
    }

    /**
     * Retrieves a UserDto by the given accountId.
     *
     * @param accountId The account ID of the user.
     * @return The UserDto object corresponding to the given accountId.
     * @throws NotFoundException If the account or user is not found on the server.
     */
    @Override
    public UserResponse readUserByAccountId(Long accountId) {
        log.info("Reading user by account ID {}", accountId);
        Long userId = accountService.readByAccountNumber(accountId).getUserId();
        try {
            var userResponse = userRepository.findById(userId)
                    .map(userMapper::toDto)
                    .orElseThrow(() -> new NotFoundException("User not found on the server"));
            log.info("Read user by account ID {} successfully", accountId);
            return userResponse;
        } catch (DataAccessException ex) {
            throw new DatabaseException("Failed to add new user to the database", ex);
        }
    }


//    public void deleteUserInfo(String email) {
//        log.info("Deleting user by email: {}", email);
//        if (!userRepository.existsByEmail(email))
//            throw new NotFoundException("User not found with ID: " + email);
//        userRepository.deleteByEmail(email);
//        log.info("Successfully deleted user with ID: {}", email);
//    }


    //to-do: Cache system
    private synchronized void validateNewUserData(UserRequestDto userRequestDto) {
        Optional<UserEntity> existingUserByEmail = userRepository.findByEmail(userRequestDto.getEmail());
        if (existingUserByEmail.isPresent())
            throw new DuplicateDataException("User with email " + userRequestDto.getEmail() + " already exists");

        Optional<UserProfileEntity> existingUserByPhoneNumber = userProfileRepository.findByPhoneNumber(userRequestDto.getUserProfile().getPhoneNumber());
        if (existingUserByPhoneNumber.isPresent())
            throw new DuplicateDataException("User with phone number " + userRequestDto.getUserProfile().getPhoneNumber() + " already exists");
    }

}
