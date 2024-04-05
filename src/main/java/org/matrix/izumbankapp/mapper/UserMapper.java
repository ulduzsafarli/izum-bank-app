package org.matrix.izumbankapp.mapper;

import org.mapstruct.*;
import org.matrix.izumbankapp.dao.entities.User;
import org.matrix.izumbankapp.model.users.*;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserMapper {
    @Mapping(source = "userProfile", target = "userProfileDto")
    UserResponse toDto(User user);
    @Mapping(source = "id", target = "id")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "cif", target = "cif")
    @Mapping(source = "role", target = "role")
    @Mapping(source = "accounts", target = "accounts")
    UserAccountsResponse toAccountsDto(User user);
    User toEntity(UserRequest userRequest);
    User toEntity(UserCreateDto userCreateDto);
    User toEntity(UserResponse userResponse);
    User updateEntityFromRequest(UserUpdateDto userCreateDto, @MappingTarget User user);
}
