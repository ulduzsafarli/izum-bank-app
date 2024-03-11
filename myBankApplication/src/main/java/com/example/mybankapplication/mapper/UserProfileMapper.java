package com.example.mybankapplication.mapper;

import com.example.mybankapplication.dao.entities.UserProfileEntity;
import com.example.mybankapplication.model.users.profile.UserProfileDto;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserProfileMapper {

    UserProfileDto toDto(UserProfileEntity userProfileEntity);

    UserProfileEntity toEntity(UserProfileDto userProfileDto);
}