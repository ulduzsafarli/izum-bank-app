package org.matrix.izumbankapp.service;

import org.matrix.izumbankapp.model.users.*;
import org.matrix.izumbankapp.model.users.profile.UserProfileDto;
import org.matrix.izumbankapp.model.users.profile.UserProfileFilterDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {
    Page<UserProfileDto> findByFilter(UserProfileFilterDto filter, Pageable pageRequest);

    UserResponse getById(Long id);

    UserResponse getByEmail(String email);

    UserResponse update(Long id, UserUpdateDto userCreateDto);

    void delete(Long id);

    UserResponse create(UserCreateDto userCreateDto);

    UserAccountsResponse getByAccountNumber(String accountNumber);

    void createCif(Long userId);

}
