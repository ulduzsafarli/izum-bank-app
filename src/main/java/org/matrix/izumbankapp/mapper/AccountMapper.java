package org.matrix.izumbankapp.mapper;

import org.mapstruct.*;
import org.matrix.izumbankapp.dao.entities.AccountEntity;
import org.matrix.izumbankapp.model.accounts.AccountRequest;
import org.matrix.izumbankapp.model.accounts.AccountCreateDto;
import org.matrix.izumbankapp.model.accounts.AccountResponse;

@Mapper(componentModel = "spring", uses = {UserMapper.class, TransactionMapper.class},
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface AccountMapper {
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "transactions", target = "transactionResponseList")
    AccountResponse toDto(AccountEntity accountEntity);

    @InheritInverseConfiguration
    AccountEntity toEntity(AccountResponse accountResponse);

    @Mapping(target = "user.id", source = "userId")
    AccountEntity fromRequestDtoForUser(AccountCreateDto accountCreateDto);

    void updateEntityFromDto(AccountRequest account, @MappingTarget AccountEntity accountEntity);
}
