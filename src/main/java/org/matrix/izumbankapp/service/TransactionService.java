package org.matrix.izumbankapp.service;

import org.matrix.izumbankapp.enumeration.transaction.TransactionStatus;
import org.matrix.izumbankapp.enumeration.transaction.TransactionType;
import org.matrix.izumbankapp.model.transactions.TransactionAccountRequest;
import org.matrix.izumbankapp.model.transactions.TransactionFilterDto;
import org.matrix.izumbankapp.model.transactions.TransactionResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TransactionService {
    List<TransactionResponse> getFromAccountId(Long accountId);
    TransactionResponse create(Long accountId, TransactionAccountRequest transactionAccountRequest,
                               TransactionType transactionType);

    void updateStatus(Long id, TransactionStatus transactionStatus);
    TransactionResponse getByID(Long transactionId);
    Page<TransactionResponse> findByFilter(TransactionFilterDto transactionFilterDto, Pageable pageable);

    TransactionResponse getByUUID(String transactionUUID);
}
