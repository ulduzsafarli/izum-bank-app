package org.matrix.izumbankapp.mapper;

import org.matrix.izumbankapp.dao.entities.UserProfileEntity;
import org.matrix.izumbankapp.model.users.profile.UserProfileDto;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserProfileMapper {

    UserProfileDto toDto(UserProfileEntity userProfileEntity);

    UserProfileEntity toEntity(UserProfileDto userProfileDto);
}