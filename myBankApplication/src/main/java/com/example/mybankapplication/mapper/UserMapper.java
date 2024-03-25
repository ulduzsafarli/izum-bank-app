package com.example.mybankapplication.mapper;

import com.example.mybankapplication.dao.entities.UserEntity;
import com.example.mybankapplication.model.accounts.AccountRequest;
import com.example.mybankapplication.model.accounts.AccountResponse;
import com.example.mybankapplication.model.users.*;
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
