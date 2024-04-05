package org.matrix.izumbankapp.mapper;

import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.matrix.izumbankapp.dao.entities.Account;
import org.matrix.izumbankapp.dao.entities.Transaction;
import org.matrix.izumbankapp.model.transactions.TransactionRequest;
import org.matrix.izumbankapp.model.transactions.TransactionResponse;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface TransactionMapper {
    @Mapping(target = "account", source = "accountId", qualifiedByName = "toAccountEntity")
    Transaction fromRequestDto(TransactionRequest transactionRequest);
    @Mapping(target = "account", source = "accountId", qualifiedByName = "toAccountEntity")
    Transaction fromResponseDto(TransactionResponse transactionResponse);
    @Mapping(source = "account.id", target = "accountId")
    TransactionResponse toResponseDto(Transaction transaction);

    @Named("toAccountEntity")
    default Account toAccountEntity(Long accountId) {
        if (accountId == null) {
            return null;
        }
        Account account = new Account(accountId);
        account.setId(accountId);
        return account;
    }
}
