package org.matrix.izumbankapp.mapper;

import org.mapstruct.*;
import org.matrix.izumbankapp.dao.entities.Account;
import org.matrix.izumbankapp.model.accounts.AccountRequest;
import org.matrix.izumbankapp.model.accounts.AccountCreateDto;
import org.matrix.izumbankapp.model.accounts.AccountResponse;

@Mapper(componentModel = "spring", uses = {UserMapper.class, TransactionMapper.class},
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface AccountMapper {
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "transactions", target = "transactionResponseList")
    AccountResponse toDto(Account account);

    @InheritInverseConfiguration
    Account toEntity(AccountResponse accountResponse);

    @Mapping(target = "user.id", source = "userId")
    Account fromRequestDtoForUser(AccountCreateDto accountCreateDto);

    Account updateEntityFromDto(AccountRequest account, @MappingTarget Account accountEntity);
}
