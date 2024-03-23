package com.example.mybankapplication.service;

import com.example.mybankapplication.model.users.profile.UserProfileDto;
import com.example.mybankapplication.model.users.profile.UserProfileFilterDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
public interface UserProfileService {
    Page<UserProfileDto> findUsersProfileByFilter(UserProfileFilterDto filterDto, Pageable pageRequest);

    void deleteUserProfileById(Long id);

    UserProfileDto getUserProfileByPhoneNumber(String phoneNumber);
    boolean existingUserprofileByPhoneNumber(String phoneNumber);

}
