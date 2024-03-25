package org.matrix.izumbankapp.mapper;

import org.matrix.izumbankapp.dao.entities.AccountEntity;
import org.matrix.izumbankapp.dao.entities.UserEntity;
import org.matrix.izumbankapp.model.accounts.AccountRequest;
import org.matrix.izumbankapp.model.accounts.AccountCreateDto;
import org.matrix.izumbankapp.model.accounts.AccountResponse;
import org.mapstruct.*;


@Mapper(componentModel = "spring", uses = {UserMapper.class},
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface AccountMapper {
    @Mapping(source = "user.id", target = "userId")
    AccountResponse toDto(AccountEntity accountEntity);
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
