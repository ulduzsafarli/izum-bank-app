package com.example.mybankapplication.mapper;

import com.example.mybankapplication.dao.entities.AccountEntity;
import com.example.mybankapplication.dao.entities.UserEntity;
import com.example.mybankapplication.model.accounts.AccountRequest;
import com.example.mybankapplication.model.accounts.AccountCreateDto;
import com.example.mybankapplication.model.accounts.AccountResponse;
import org.mapstruct.*;


@Mapper(componentModel = "spring", uses = {UserMapper.class},
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface AccountMapper {
    @Mapping(source = "user.id", target = "userId")
    AccountResponse toDto(AccountEntity accountEntity);

//    AccountEntity fromRequestDto(AccountRequest accountRequest);
    AccountEntity fromResponseDto(AccountResponse accountResponse);
    @Mapping(target = "user", source = "userId", qualifiedByName = "toUserEntity")
    AccountEntity fromRequestDtoForUser(AccountCreateDto accountCreateDto);

    @Named("toUserEntity")
    default UserEntity toUserEntity(Long userId) {
        if (userId == null) {
            return null;
        }
        UserEntity userEntity = new UserEntity();
        userEntity.setId(userId);
        return userEntity;
    }

    AccountEntity updateEntityFromDto(AccountRequest account, @MappingTarget AccountEntity accountEntity);
}
