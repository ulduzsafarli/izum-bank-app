package org.matrix.izumbankapp.service;

import org.matrix.izumbankapp.model.auth.ResponseDto;
import org.matrix.izumbankapp.model.users.*;
import org.matrix.izumbankapp.model.users.profile.UserProfileDto;
import jakarta.transaction.Transactional;
import org.matrix.izumbankapp.model.users.profile.UserProfileFilterDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {
    Page<UserProfileDto> findUsersByFilter(UserProfileFilterDto filter, Pageable pageRequest);

    List<UserResponse> getAllUser();

    UserResponse getUserById(Long id);

    UserAccountsResponse getUserByIdForAccount(Long id);

    UserResponse getUserByEmail(String email);

    @Transactional
    ResponseDto updateUser(Long id, UserUpdateDto userCreateDto);

    @Transactional
    ResponseDto deleteUserById(Long id);

    @Transactional
    ResponseDto addUser(UserCreateDto userCreateDto);


    void createCif(Long userId);

}
