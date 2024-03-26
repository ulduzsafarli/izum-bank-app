package org.matrix.izumbankapp.mapper;

import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.matrix.izumbankapp.dao.entities.AccountEntity;
import org.matrix.izumbankapp.dao.entities.TransactionEntity;
import org.matrix.izumbankapp.model.transactions.TransactionRequest;
import org.matrix.izumbankapp.model.transactions.TransactionResponse;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface TransactionMapper {
    @Mapping(target = "account", source = "accountId", qualifiedByName = "toAccountEntity")
    TransactionEntity fromRequestDto(TransactionRequest transactionRequest);
    @Mapping(target = "account", source = "accountId", qualifiedByName = "toAccountEntity")
    TransactionEntity fromResponseDto(TransactionResponse transactionResponse);
    @Mapping(source = "account.id", target = "accountId")
    TransactionResponse toResponseDto(TransactionEntity transactionEntity);

    @Named("toAccountEntity")
    default AccountEntity toAccountEntity(Long accountId) {
        if (accountId == null) {
            return null;
        }
        AccountEntity accountEntity = new AccountEntity();
        accountEntity.setId(accountId);
        return accountEntity;
    }
}
