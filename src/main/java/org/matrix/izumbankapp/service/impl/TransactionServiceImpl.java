package org.matrix.izumbankapp.service.impl;

import org.matrix.izumbankapp.dao.entities.TransactionEntity;
import org.matrix.izumbankapp.dao.repository.TransactionRepository;
import org.matrix.izumbankapp.enumeration.transaction.TransactionStatus;
import org.matrix.izumbankapp.enumeration.transaction.TransactionType;
import org.matrix.izumbankapp.exception.DatabaseException;
import org.matrix.izumbankapp.exception.NotFoundException;
import org.matrix.izumbankapp.mapper.TransactionMapper;
import org.matrix.izumbankapp.model.transactions.TransactionAccountRequest;
import org.matrix.izumbankapp.model.transactions.TransactionFilterDto;
import org.matrix.izumbankapp.model.transactions.TransactionRequest;
import org.matrix.izumbankapp.model.transactions.TransactionResponse;
import org.matrix.izumbankapp.service.TransactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.matrix.izumbankapp.util.specifications.TransactionSpecifications;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;

    private static final String WITH_ID_NOT_FOUND = "Account with ID %d not found."; //change
    private static final String WITH_ACCOUNT_ID_NOT_FOUND = "Transactions for account ID %s not found.";

    @Override
    public List<TransactionResponse> getTransactionsFromAccountId(Long accountId) {
        log.info("Receiving transactions for account ID {}", accountId);

        var transactionResponses = transactionRepository.findByAccountId(accountId)
                .orElseThrow(() -> new NotFoundException(String.format(WITH_ACCOUNT_ID_NOT_FOUND, accountId)))
                .stream().map(transactionMapper::toResponseDto).toList();

        if (transactionResponses.isEmpty()) {
            throw new NotFoundException("Transactions not found for account ID: " + accountId);
        }

        log.info("Successfully receive transactions for account ID {}", accountId);
        return transactionResponses;
    }

    @Override
    public TransactionResponse createTransaction(Long accountId,
                                                 TransactionAccountRequest transactionAccountRequest,
                                                 TransactionType transactionType) {
        log.info("Creating transaction for account number {} for transferring money, details: {}", accountId, transactionAccountRequest);
        TransactionRequest transactionRequest = TransactionRequest.builder()
                .amount(transactionAccountRequest.getAmount())
                .comments(transactionAccountRequest.getComments())
                .type(transactionType)
                .status(TransactionStatus.PENDING)
                .transactionUUID(UUID.randomUUID().toString())
                .accountId(accountId).build();
        var transactionEntity = transactionMapper.fromRequestDto(transactionRequest);
        transactionRepository.save(transactionEntity);
        log.info("Successfully create transaction for account ID {} for transferring money, details: {}", accountId, transactionAccountRequest);
        return transactionMapper.toResponseDto(transactionEntity);
    }

    @Override
    public void updateTransactionStatus(Long id, TransactionStatus transactionStatus) {
        log.info("Updating status for transaction ID {} to status {}", id, transactionStatus);
        var transaction = getTransactionById(id);
        transaction.setStatus(transactionStatus);
        transactionRepository.save(transactionMapper.fromResponseDto(transaction));
        log.info("Successfully update status for transaction ID {} to status {}", id, transactionStatus);
    }

    @Override
    public TransactionResponse getTransactionsByID(Long transactionID) {
        log.info("Receiving all {} transactions", transactionID);
        var transaction = transactionRepository.findById(transactionID)
                .orElseThrow(() -> new NotFoundException("Transaction with type " + transactionID + " not found"));
        log.info("Successfully receive all {} transactions", transactionID);
        return transactionMapper.toResponseDto(transaction);
    }


    @Override
    public Page<TransactionResponse> findTransactionByFilter(TransactionFilterDto transactionFilterDto, Pageable pageable) {
        log.info("Searching transactions by filter: {}", transactionFilterDto);
        try {
            Specification<TransactionEntity> accountSpecification = TransactionSpecifications.getAccountSpecification(transactionFilterDto);
            Page<TransactionEntity> transactionsPage = transactionRepository.findAll(accountSpecification, pageable);
            log.info("Successfully found accounts");
            return transactionsPage.map(transactionMapper::toResponseDto);
        } catch (DataAccessException ex) {
            throw new DatabaseException("Failed to find an account in the database", ex);
        }
    }

    @Override
    public TransactionResponse getTransactionByUUID(String transactionUUID) {
        log.info("Receiving transaction by transactionUUID {}", transactionUUID);
        var transaction = transactionRepository.findByTransactionUUID(transactionUUID)
                .orElseThrow(() -> new NotFoundException("Transactions not found for account ID: " + transactionUUID));
        log.info("Successfully receive transaction by transactionUUID {}", transactionUUID);
        return transactionMapper.toResponseDto(transaction);
    }

    public TransactionResponse getTransactionById(Long id) {
        log.info("Receiving transaction by ID {}", id);
        var transaction = transactionRepository.findById(id).map(transactionMapper::toResponseDto)
                .orElseThrow(() -> new NotFoundException("Transaction with this ID " + id + " not found"));
        log.info("Successfully receive transaction by ID {}", id);
        return transaction;
    }
}
