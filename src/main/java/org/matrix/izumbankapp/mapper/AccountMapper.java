package org.matrix.izumbankapp.mapper;

import org.matrix.izumbankapp.dao.entities.AccountEntity;
import org.matrix.izumbankapp.dao.entities.UserEntity;
import org.matrix.izumbankapp.model.accounts.AccountRequest;
import org.matrix.izumbankapp.model.accounts.AccountCreateDto;
import org.matrix.izumbankapp.model.accounts.AccountResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", uses = {UserMapper.class, TransactionMapper.class},
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface AccountMapper {
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "transactions", target = "transactionResponseList")
    AccountResponse toDto(AccountEntity accountEntity);

    AccountEntity fromResponseDto(AccountResponse accountResponse);

    @Mapping(target = "user", source = "userId", qualifiedByName = "toUserEntity")
    AccountEntity fromRequestDtoForUser(AccountCreateDto accountCreateDto);

    @Named("toUserEntity")
    default UserEntity toUserEntity(Long userId) {
        return userId != null ? new UserEntity(userId) : null;
    }

    void updateEntityFromDto(AccountRequest account, @MappingTarget AccountEntity accountEntity);

    AccountEntity toEntity(AccountCreateDto accountCreateDto);
}