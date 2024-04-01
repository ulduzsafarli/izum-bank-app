package org.matrix.izumbankapp.service;

import org.matrix.izumbankapp.enumeration.transaction.TransactionStatus;
import org.matrix.izumbankapp.enumeration.transaction.TransactionType;
import org.matrix.izumbankapp.model.transactions.TransactionAccountRequest;
import org.matrix.izumbankapp.model.transactions.TransactionFilterDto;
import org.matrix.izumbankapp.model.transactions.TransactionResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TransactionService {
    List<TransactionResponse> getTransactionsFromAccountId(Long accountId);
    TransactionResponse createTransaction(Long accountId, TransactionAccountRequest transactionAccountRequest,
                                          TransactionType transactionType);

    void updateTransactionStatus(Long id, TransactionStatus transactionStatus);
    TransactionResponse getTransactionsByID(Long transactionId);
    Page<TransactionResponse> findTransactionByFilter(TransactionFilterDto transactionFilterDto, Pageable pageable);

    TransactionResponse getTransactionByUUID(String transactionUUID);
}
