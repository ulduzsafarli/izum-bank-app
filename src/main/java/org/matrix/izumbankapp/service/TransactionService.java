package org.matrix.izumbankapp.service;

import org.matrix.izumbankapp.enumeration.transaction.TransactionStatus;
import org.matrix.izumbankapp.model.transactions.TransactionAccountRequest;
import org.matrix.izumbankapp.model.transactions.TransactionResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TransactionService {
    List<TransactionResponse> getTransactionsFromAccountId(Long accountId);
    TransactionResponse createTransactionForTransferring(Long accountId, TransactionAccountRequest transactionAccountRequest);

    void updateTransactionStatus(Long id, TransactionStatus transactionStatus);
}
