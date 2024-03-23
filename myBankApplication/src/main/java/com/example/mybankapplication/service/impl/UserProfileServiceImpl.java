package com.example.mybankapplication.service.impl;

import com.example.mybankapplication.dao.entities.UserProfileEntity;
import com.example.mybankapplication.dao.repository.UserProfileRepository;
import com.example.mybankapplication.exception.NotFoundException;
import com.example.mybankapplication.mapper.UserProfileMapper;
import com.example.mybankapplication.model.users.profile.UserProfileDto;
import com.example.mybankapplication.model.users.profile.UserProfileFilterDto;
import com.example.mybankapplication.service.UserProfileService;
import com.example.mybankapplication.util.specifications.UserProfileSpecifications;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserProfileServiceImpl implements UserProfileService {
    private final UserProfileMapper userProfileMapper;
    private final UserProfileRepository userProfileRepository;

    @Override
    public Page<UserProfileDto> findUsersProfileByFilter(UserProfileFilterDto filterDto, Pageable pageRequest) {
        Specification<UserProfileEntity> userProfileSpecification = UserProfileSpecifications.getUserProfileSpecification(filterDto);
        Page<UserProfileEntity> userProfileEntity = userProfileRepository.findAll(userProfileSpecification, pageRequest);
        log.info("Successfully found users");
        return userProfileEntity.map(userProfileMapper::toDto);
    }

    @Override
    public void deleteUserProfileById(Long id) {
        userProfileRepository.deleteById(id);
        log.info("Successfully deleted users profile");
    }

    @Override
    public UserProfileDto getUserProfileByPhoneNumber(String phoneNumber) {
        return userProfileRepository.findByPhoneNumber(phoneNumber).map(userProfileMapper::toDto)
                .orElseThrow(() -> new NotFoundException("User profile with phone number " + phoneNumber + " not found "));
    }

    @Override
    public boolean existingUserprofileByPhoneNumber(String phoneNumber) {
        return userProfileRepository.existsByPhoneNumber(phoneNumber);
    }


}
