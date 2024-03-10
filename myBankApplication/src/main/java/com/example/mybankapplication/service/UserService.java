package com.example.mybankapplication.service;

import com.example.mybankapplication.model.users.UserFilterDto;
import com.example.mybankapplication.model.users.UserRequest;
import com.example.mybankapplication.model.users.UserResponse;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {
    Page<UserResponse> findUsersByFilter(UserFilterDto filterDto, Pageable pageRequest);
    List<UserResponse> getAllUser();
    UserResponse getUserById(Long id);
    UserResponse getUserByEmail(String email);
    UserResponse getUserByPhoneNumber(String phoneNumber);
    @Transactional
    void updateUser(Long id, UserRequest userRequest);
    @Transactional
    void deleteUserById(Long id);
    void addUser(UserRequest userRequest);

//    void deleteUserInfo(String email);
}
