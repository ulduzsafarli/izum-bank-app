package com.example.mybankapplication.service.impl;

import com.example.mybankapplication.dao.entities.UserEntity;
import com.example.mybankapplication.exception.*;
import com.example.mybankapplication.mapper.UserMapper;
import com.example.mybankapplication.model.users.*;
import com.example.mybankapplication.dao.repository.UserRepository;
import com.example.mybankapplication.service.UserService;
import com.example.mybankapplication.specifications.UserSpecifications;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public Page<UserResponse> findUsersByFilter(UserFilterDto filterDto, Pageable pageRequest) {
        log.info("Searching users by filter: {}", filterDto);
        Specification<UserEntity> userSpecification = UserSpecifications.getUserSpecification(filterDto);
        Page<UserEntity> userEntityPage = userRepository.findAll(userSpecification, pageRequest);
        log.info("Successfully found users");
        return userEntityPage.map(userMapper::toDto);
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
    public UserResponse getUserById(Long id) {
        log.info("Retrieving user by ID: {}", id);
        UserEntity userEntity = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found with ID: " + id));
        UserResponse userResponse = userMapper.toDto(userEntity);
        log.info("Successfully retrieved User with ID: {}", id);
        return userResponse;
    }

    @Override
    public UserResponse getUserByEmail(String email) {
        log.info("Retrieving user by email: {}", email);
        UserEntity userEntity = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User not found with email: " + email));
        UserResponse userResponse = userMapper.toDto(userEntity);
        log.info("Successfully retrieved user with email: {}", email);
        return userResponse;
    }

    @Override
    public UserResponse getUserByPhoneNumber(String phoneNumber) {
        log.info("Retrieving user by phone number: {}", phoneNumber);
        UserEntity userEntity = userRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new NotFoundException("User not found with phone number: " + phoneNumber));
        UserResponse userResponse = userMapper.toDto(userEntity);
        log.info("Successfully retrieved user with phone number: {}", phoneNumber);
        return userResponse;
    }

    @Override
    public void updateUser(Long id, UserRequest userRequest) {
        log.info("Updating user with ID {} to: {}", id, userRequest);
        UserEntity userEntity = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found with ID: " + id));
        userEntity = userMapper.updateEntityFromRequest(userRequest, userEntity);
        userRepository.save(userEntity);
        log.info("Successfully updated user with ID: {}", id);
    }

    @Override
    public void deleteUserById(Long id) {
        log.info("Deleting user by ID: {}", id);
        if (!userRepository.existsById(id))
            throw new NotFoundException("User not found with ID: " + id);
        userRepository.deleteById(id);
        log.info("Successfully deleted user with ID: {}", id);
    }

    @Override
    public void addUser(UserRequest userRequest) {
        log.info("Adding new user: {}", userRequest);
        validateNewUserData(userRequest);
        try {
            userRepository.save(userMapper.toEntity(userRequest));
            log.info("Successfully added new user");
        } catch (DataAccessException ex) {
            throw new DatabaseException("Failed to add new user to the database", ex);
        }
    }

    private synchronized void validateNewUserData(UserRequest userRequest) {
        Optional<UserEntity> existingUserByEmail = userRepository.findByEmail(userRequest.getEmail());
        if (existingUserByEmail.isPresent())
            throw new DuplicateDataException("User with email " + userRequest.getEmail() + " already exists");

        Optional<UserEntity> existingUserByPhoneNumber = userRepository.findByPhoneNumber(userRequest.getPhoneNumber());
        if (existingUserByPhoneNumber.isPresent())
            throw new DuplicateDataException("User with phone number " + userRequest.getPhoneNumber() + " already exists");
    }

}
