package org.matrix.izumbankapp.mapper;

import org.matrix.izumbankapp.dao.entities.UserProfile;
import org.matrix.izumbankapp.model.users.profile.UserProfileDto;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserProfileMapper {

    UserProfileDto toDto(UserProfile userProfile);

    UserProfile toEntity(UserProfileDto userProfileDto);
}