package com.example.mybankapplication.service.impl;

import com.example.mybankapplication.dao.repository.TransactionRepository;
import com.example.mybankapplication.enumeration.transaction.TransactionStatus;
import com.example.mybankapplication.enumeration.transaction.TransactionType;
import com.example.mybankapplication.exception.NotFoundException;
import com.example.mybankapplication.mapper.TransactionMapper;
import com.example.mybankapplication.model.accounts.AccountResponse;
import com.example.mybankapplication.model.auth.ResponseDto;
import com.example.mybankapplication.model.transactions.TransactionAccountRequest;
import com.example.mybankapplication.model.transactions.TransactionRequest;
import com.example.mybankapplication.model.transactions.TransactionResponse;
import com.example.mybankapplication.service.TransactionService;
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

    @Override
    public List<TransactionResponse> getTransactionsByAccount(AccountResponse account) {
//        return transactionRepository.findById(account.getTransactionResponseList());
        return null;
    }

    @Override
    public TransactionResponse createTransactionForTransferring(Long accountId, TransactionAccountRequest transactionAccountRequest) {
        log.info("Creating transaction for account ID {} for transferring money, details: {}", accountId, transactionAccountRequest);
        TransactionRequest transactionRequest = TransactionRequest.builder()
                .amount(transactionAccountRequest.getAmount())
                .comments(transactionAccountRequest.getComments())
                .type(TransactionType.TRANSFER)
                .status(TransactionStatus.PENDING)
                .transactionUUID(UUID.randomUUID().toString()).build();
        var transactionEntity = transactionMapper.fromRequestDto(transactionRequest);
        transactionRepository.save(transactionEntity);
        log.info("Successfully create transaction for account ID {} for transferring money, details: {}", accountId, transactionAccountRequest);
        return transactionMapper.toResponseDTo(transactionEntity);
    }

    @Override
    public ResponseDto updateTransactionStatus(Long id, TransactionStatus transactionStatus) {
        log.info("Updating status for transaction ID {} to status {}", id, transactionStatus);
        var transaction = getTransactionById(id);
        transaction.setStatus(transactionStatus);
        transactionRepository.save(transactionMapper.fromResponseDto(transaction));
        log.info("Successfully update status for transaction ID {} to status {}", id, transactionStatus);
        return ResponseDto.builder().responseMessage("Successfully updated status").build();
    }

    public TransactionResponse getTransactionById(Long id){
        log.info("Retrieving transaction by ID {}", id);
        var transaction = transactionRepository.findById(id).map(transactionMapper::toResponseDTo)
                        .orElseThrow(() -> new NotFoundException("Transaction with this ID " + id + " not found"));
        log.info("Successfully retrieve transaction by ID {}", id);
        return transaction;
    }
}
