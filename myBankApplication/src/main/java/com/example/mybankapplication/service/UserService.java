package com.example.mybankapplication.service;

import com.example.mybankapplication.model.auth.ResponseDto;
import com.example.mybankapplication.model.users.UserRequestDto;
import com.example.mybankapplication.model.users.profile.UserProfileFilterDto;
import com.example.mybankapplication.model.users.UserRequest;
import com.example.mybankapplication.model.users.UserResponse;
import com.example.mybankapplication.model.users.profile.UserProfileDto;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {
    Page<UserProfileDto> findUsersByFilter(UserProfileFilterDto filterDto, Pageable pageRequest);
    List<UserResponse> getAllUser();
    UserResponse readUserById(Long id);
    UserResponse readUserByEmail(String email);
    @Transactional
    ResponseDto updateUser(Long id, UserRequestDto userRequestDto);
    @Transactional
    ResponseDto deleteUserById(Long id);
    ResponseDto addUser(UserRequestDto userRequestDto);

    UserResponse readUserByAccountId(Long accountId);

//    void deleteUserInfo(String email);
}
