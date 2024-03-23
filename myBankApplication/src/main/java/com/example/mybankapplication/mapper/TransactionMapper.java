package com.example.mybankapplication.mapper;

import com.example.mybankapplication.dao.entities.TransactionEntity;
import com.example.mybankapplication.model.transactions.TransactionRequest;
import com.example.mybankapplication.model.transactions.TransactionResponse;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface TransactionMapper {
    TransactionEntity fromRequestDto(TransactionRequest transactionRequest);
    TransactionEntity fromResponseDto(TransactionResponse transactionResponse);
    TransactionResponse toResponseDTo(TransactionEntity transactionEntity);
}
