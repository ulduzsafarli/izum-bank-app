package com.example.mybankapplication.mapper;

import com.example.mybankapplication.dao.entities.TransactionEntity;
import com.example.mybankapplication.model.transactions.TransactionRequest;
import com.example.mybankapplication.model.transactions.TransactionResponse;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-03-25T00:48:56+0400",
    comments = "version: 1.5.5.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-8.5.jar, environment: Java 17.0.10 (Amazon.com Inc.)"
)
@Component
public class TransactionMapperImpl implements TransactionMapper {

    @Override
    public TransactionEntity fromRequestDto(TransactionRequest transactionRequest) {
        if ( transactionRequest == null ) {
            return null;
        }

        TransactionEntity transactionEntity = new TransactionEntity();

        transactionEntity.setAmount( transactionRequest.getAmount() );
        transactionEntity.setType( transactionRequest.getType() );
        transactionEntity.setStatus( transactionRequest.getStatus() );
        transactionEntity.setTransactionUUID( transactionRequest.getTransactionUUID() );
        transactionEntity.setComments( transactionRequest.getComments() );

        return transactionEntity;
    }

    @Override
    public TransactionEntity fromResponseDto(TransactionResponse transactionResponse) {
        if ( transactionResponse == null ) {
            return null;
        }

        TransactionEntity transactionEntity = new TransactionEntity();

        transactionEntity.setId( transactionResponse.getId() );
        transactionEntity.setAmount( transactionResponse.getAmount() );
        transactionEntity.setType( transactionResponse.getType() );
        transactionEntity.setStatus( transactionResponse.getStatus() );
        transactionEntity.setTransactionUUID( transactionResponse.getTransactionUUID() );
        transactionEntity.setComments( transactionResponse.getComments() );

        return transactionEntity;
    }

    @Override
    public TransactionResponse toResponseDTo(TransactionEntity transactionEntity) {
        if ( transactionEntity == null ) {
            return null;
        }

        TransactionResponse transactionResponse = new TransactionResponse();

        transactionResponse.setId( transactionEntity.getId() );
        transactionResponse.setAmount( transactionEntity.getAmount() );
        transactionResponse.setType( transactionEntity.getType() );
        transactionResponse.setStatus( transactionEntity.getStatus() );
        transactionResponse.setTransactionUUID( transactionEntity.getTransactionUUID() );
        transactionResponse.setComments( transactionEntity.getComments() );

        return transactionResponse;
    }
}
