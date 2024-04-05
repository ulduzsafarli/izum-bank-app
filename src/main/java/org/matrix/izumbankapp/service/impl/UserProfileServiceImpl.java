package org.matrix.izumbankapp.service.impl;

import org.matrix.izumbankapp.dao.entities.UserProfile;
import org.matrix.izumbankapp.dao.repository.UserProfileRepository;
import org.matrix.izumbankapp.mapper.UserProfileMapper;
import org.matrix.izumbankapp.model.users.profile.UserProfileDto;
import org.matrix.izumbankapp.model.users.profile.UserProfileFilterDto;
import org.matrix.izumbankapp.service.UserProfileService;
import org.matrix.izumbankapp.util.specifications.UserProfileSpecifications;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserProfileServiceImpl implements UserProfileService {
    private final UserProfileMapper userProfileMapper;
    private final UserProfileRepository userProfileRepository;

    @Override
    public Page<UserProfileDto> findByFilter(UserProfileFilterDto filterDto, Pageable pageRequest) {
        Specification<UserProfile> userProfileSpecification = UserProfileSpecifications.getUserProfileSpecification(filterDto);
        Page<UserProfile> userProfileEntity = userProfileRepository.findAll(userProfileSpecification, pageRequest);
        log.info("Successfully found users");
        return userProfileEntity.map(userProfileMapper::toDto);
    }

    @Override
    public boolean existsByPhoneNumber(String phoneNumber) {
        return userProfileRepository.existsByPhoneNumber(phoneNumber);
    }


}
