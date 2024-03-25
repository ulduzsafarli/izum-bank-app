package com.example.mybankapplication.service;

import com.example.mybankapplication.enumeration.transaction.TransactionStatus;
import com.example.mybankapplication.model.accounts.AccountResponse;
import com.example.mybankapplication.model.transactions.TransactionAccountRequest;
import com.example.mybankapplication.model.transactions.TransactionResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TransactionService {
    List<TransactionResponse> getTransactionsByAccount(AccountResponse account);

    TransactionResponse createTransactionForTransferring(String accountNumber, TransactionAccountRequest transactionAccountRequest);

    void updateTransactionStatus(Long id, TransactionStatus transactionStatus);
}
