package org.matrix.izumbankapp.mapper;

import org.matrix.izumbankapp.dao.entities.UserEntity;
import org.matrix.izumbankapp.model.accounts.AccountRequest;
import org.matrix.izumbankapp.model.users.*;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserMapper {
    UserResponse toDto(UserEntity userEntity);
    UserAccountsResponse toAccountsDto(UserEntity userEntity);

    UserEntity toEntity(UserRequest userRequest);
    UserEntity toEntity(UserCreateDto userCreateDto);

    UserEntity toEntity(UserResponse userResponse);

    List<UserResponse> toDtoList(List<UserEntity> userEntityList);

    UserEntity updateEntityFromRequest(UserUpdateDto userCreateDto, @MappingTarget UserEntity userEntity);

    UserEntity fromUserAccountsDto(UserAccountsResponse userAccountsResponse);

    UserEntity toEntityForAccount(UserResponse user, AccountRequest account);

    UserEntity mapUserForAccount(UserAccountsResponse user);
}
