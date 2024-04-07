package org.matrix.izumbankapp.service.impl;

import org.matrix.izumbankapp.dao.entities.Transaction;
import org.matrix.izumbankapp.dao.repository.TransactionRepository;
import org.matrix.izumbankapp.enumeration.transaction.TransactionStatus;
import org.matrix.izumbankapp.enumeration.transaction.TransactionType;
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

    private static final String WITH_ACCOUNT_ID_NOT_FOUND = "Transactions for account ID %s not found.";

    @Override
    public List<TransactionResponse> getFromAccountId(Long accountId) {
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
    public TransactionResponse create(Long accountId,
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
    public void updateStatus(Long id, TransactionStatus transactionStatus) {
        log.info("Updating status for transaction ID {} to status {}", id, transactionStatus);
        var transaction = getByID(id);
        transaction.setStatus(transactionStatus);
        transactionRepository.save(transactionMapper.fromResponseDto(transaction));
        log.info("Successfully update status for transaction ID {} to status {}", id, transactionStatus);
    }

    @Override
    public TransactionResponse getByID(Long id) {
        log.info("Receiving all {} transactions", id);
        var transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Transaction with type " + id + " not found"));
        log.info("Successfully receive all {} transactions", id);
        return transactionMapper.toResponseDto(transaction);
    }

    @Override
    public Page<TransactionResponse> findByFilter(TransactionFilterDto transactionFilterDto, Pageable pageable) {
        log.info("Searching transactions by filter: {}", transactionFilterDto);
        Specification<Transaction> accountSpecification = TransactionSpecifications.getAccountSpecification(transactionFilterDto);
        Page<Transaction> transactionsPage = transactionRepository.findAll(accountSpecification, pageable);
        log.info("Successfully found accounts");
        return transactionsPage.map(transactionMapper::toResponseDto);
    }

    @Override
    public TransactionResponse getByUUID(String transactionUUID) {
        log.info("Receiving transaction by transactionUUID {}", transactionUUID);
        var transaction = transactionRepository.findByTransactionUUID(transactionUUID)
                .orElseThrow(() -> new NotFoundException("Transactions not found for account ID: " + transactionUUID));
        log.info("Successfully receive transaction by transactionUUID {}", transactionUUID);
        return transactionMapper.toResponseDto(transaction);
    }

}
