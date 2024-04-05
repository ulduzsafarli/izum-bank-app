package org.matrix.izumbankapp.mapper;

import org.mapstruct.*;
import org.matrix.izumbankapp.dao.entities.UserEntity;
import org.matrix.izumbankapp.model.users.*;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserMapper {
    @Mapping(source = "userProfile", target = "userProfileDto")
    UserResponse toDto(UserEntity userEntity);
    @Mapping(source = "id", target = "id")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "cif", target = "cif")
    @Mapping(source = "role", target = "role")
    @Mapping(source = "accounts", target = "accounts")
    UserAccountsResponse toAccountsDto(UserEntity userEntity);
    UserEntity toEntity(UserRequest userRequest);
    UserEntity toEntity(UserCreateDto userCreateDto);
    UserEntity toEntity(UserResponse userResponse);
    UserEntity updateEntityFromRequest(UserUpdateDto userCreateDto, @MappingTarget UserEntity userEntity);
}
