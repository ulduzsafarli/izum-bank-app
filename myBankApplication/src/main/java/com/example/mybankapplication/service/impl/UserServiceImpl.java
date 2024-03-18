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
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;
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
        List<UserResponse> userResponse = userRepository.findAll().stream()
                .map(userMapper::toDto).toList();
        log.info("Successfully retrieved all users");
        return userResponse;
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
    public UserAccountsDto getUserByIdForAccount(Long id) {
        log.info("Retrieving user by ID: {}", id);
        UserEntity userEntity = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found with ID: " + id));
        UserAccountsDto userResponse = userMapper.toAccountsDto(userEntity);
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
    public ResponseDto updateUser(Long id, UserUpdateDto userUpdateDto) {
        log.info("Updating user with ID {} to: {}", id, userUpdateDto);
        UserEntity userEntity = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found with ID: " + id));
        userEntity = userMapper.updateEntityFromRequest(userUpdateDto, userEntity);
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
        userProfileRepository.deleteById(id);
        log.info("Successfully deleted user with ID: {}", id);
        return ResponseDto.builder()
                .responseMessage("User deleted successfully")
                .responseCode(responseCodeNoContent).build();
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
                    .responseMessage("User created successfully")
                    .responseCode(responseCodeSuccess).build();
        } catch (DataAccessException ex) {
            throw new DatabaseException("Failed to add new user to the database", ex);
        }
    }

    @Override
    public String generateCif() {
        log.info("Generating unique cif for user");
        do {
            String randomNumber = String.valueOf(ThreadLocalRandom.current().nextInt(10000, 100000));
            boolean isUnique = !userRepository.existsByCif(randomNumber);
            if (isUnique) {
                log.info("CIF generated successfully {}", randomNumber);
                return randomNumber;
            }
        } while (true);
    }


//    public void deleteUserInfo(String email) {
//        log.info("Deleting user by email: {}", email);
//        if (!userRepository.existsByEmail(email))
//            throw new NotFoundException("User not found with ID: " + email);
//        userRepository.deleteByEmail(email);
//        log.info("Successfully deleted user with ID: {}", email);
//    }


    //to-do: Cache system
    private synchronized void validateNewUserData(UserCreateDto userCreateDto) {
        Optional<UserEntity> existingUserByEmail = userRepository.findByEmail(userCreateDto.getEmail());
        if (existingUserByEmail.isPresent())
            throw new DuplicateDataException("User with email " + userCreateDto.getEmail() + " already exists");

        Optional<UserProfileEntity> existingUserByPhoneNumber = userProfileRepository.findByPhoneNumber(userCreateDto.getUserProfile().getPhoneNumber());
        if (existingUserByPhoneNumber.isPresent())
            throw new DuplicateDataException("User with phone number " + userCreateDto.getUserProfile().getPhoneNumber() + " already exists");
    }

}
