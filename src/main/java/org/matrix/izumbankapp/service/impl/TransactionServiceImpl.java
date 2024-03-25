package org.matrix.izumbankapp.service.impl;

import org.matrix.izumbankapp.dao.entities.TransactionEntity;
import org.matrix.izumbankapp.dao.repository.TransactionRepository;
import org.matrix.izumbankapp.enumeration.transaction.TransactionStatus;
import org.matrix.izumbankapp.enumeration.transaction.TransactionType;
import org.matrix.izumbankapp.exception.NotFoundException;
import org.matrix.izumbankapp.mapper.TransactionMapper;
import org.matrix.izumbankapp.model.accounts.AccountResponse;
import org.matrix.izumbankapp.model.auth.ResponseDto;
import org.matrix.izumbankapp.model.transactions.TransactionAccountRequest;
import org.matrix.izumbankapp.model.transactions.TransactionRequest;
import org.matrix.izumbankapp.model.transactions.TransactionResponse;
import org.matrix.izumbankapp.service.TransactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;

    private static final String WITH_ID_NOT_FOUND = "Account with ID not found: "; //change

    @Override
    public List<TransactionResponse> getTransactionsFromAccountId(Long accountId) {
        log.info("Retrieving transactions for account ID {}", accountId);

        var transactionResponses = transactionRepository.findByAccountId(accountId)
                .orElseThrow(() -> new NotFoundException("Transactions not found for account ID: " + accountId))
                .stream().map(transactionMapper::toResponseDTo).toList();

        log.info("Successfully retrieved transactions for account ID {}", accountId);
        return transactionResponses;
    }

    @Override
    public TransactionResponse createTransferTransaction(Long accountId, TransactionAccountRequest transactionAccountRequest) {
        log.info("Creating transaction for account number {} for transferring money, details: {}", accountId, transactionAccountRequest);
        TransactionRequest transactionRequest = TransactionRequest.builder()
                .amount(transactionAccountRequest.getAmount())
                .comments(transactionAccountRequest.getComments())
                .type(TransactionType.TRANSFER)
                .status(TransactionStatus.PENDING)
                .transactionUUID(UUID.randomUUID().toString())
                .accountId(accountId).build();
        var transactionEntity = transactionMapper.fromRequestDto(transactionRequest);
        transactionRepository.save(transactionEntity);
        log.info("Successfully create transaction for account ID {} for transferring money, details: {}", accountId, transactionAccountRequest);
        return transactionMapper.toResponseDTo(transactionEntity);
    }

    @Override
    public void updateTransactionStatus(Long id, TransactionStatus transactionStatus) {
        log.info("Updating status for transaction ID {} to status {}", id, transactionStatus);
        var transaction = getTransactionById(id);
        transaction.setStatus(transactionStatus);
        transactionRepository.save(transactionMapper.fromResponseDto(transaction));
        log.info("Successfully update status for transaction ID {} to status {}", id, transactionStatus);
    }

    public TransactionResponse getTransactionById(Long id) {
        log.info("Retrieving transaction by ID {}", id);
        var transaction = transactionRepository.findById(id).map(transactionMapper::toResponseDTo)
                .orElseThrow(() -> new NotFoundException("Transaction with this ID " + id + " not found"));
        log.info("Successfully retrieve transaction by ID {}", id);
        return transaction;
    }
}
