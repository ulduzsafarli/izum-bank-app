package com.example.mybankapplication.service;

import com.example.mybankapplication.model.auth.ResponseDto;
import com.example.mybankapplication.model.users.*;
import com.example.mybankapplication.model.users.profile.UserProfileFilterDto;
import com.example.mybankapplication.model.users.profile.UserProfileDto;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {
    Page<UserProfileDto> findUsersByFilter(UserFilteringDto filterDto, Pageable pageRequest);
    List<UserResponse> getAllUser();
    UserResponse getUserById(Long id);
    UserAccountsDto getUserByIdForAccount(Long id);
    UserResponse getUserByEmail(String email);
    @Transactional
    ResponseDto updateUser(Long id, UserUpdateDto userCreateDto);
    @Transactional
    ResponseDto deleteUserById(Long id);
    @Transactional
    ResponseDto addUser(UserCreateDto userCreateDto);


//    void deleteUserInfo(String email);
}
