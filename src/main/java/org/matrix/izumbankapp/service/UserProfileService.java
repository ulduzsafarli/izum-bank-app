package org.matrix.izumbankapp.service;

import org.matrix.izumbankapp.model.users.profile.UserProfileDto;
import org.matrix.izumbankapp.model.users.profile.UserProfileFilterDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
public interface UserProfileService {
    Page<UserProfileDto> findByFilter(UserProfileFilterDto filterDto, Pageable pageRequest);
    boolean existsByPhoneNumber(String phoneNumber);

}
