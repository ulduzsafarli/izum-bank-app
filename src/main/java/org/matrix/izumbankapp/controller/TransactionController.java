package org.matrix.izumbankapp.controller;

import lombok.RequiredArgsConstructor;
import org.matrix.izumbankapp.enumeration.transaction.TransactionType;
import org.matrix.izumbankapp.model.transactions.TransactionFilterDto;
import org.matrix.izumbankapp.model.transactions.TransactionResponse;
import org.matrix.izumbankapp.service.TransactionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1//accounts")
public class TransactionController {
    TransactionService transactionService;
    @GetMapping("{accountId}/transactions")
    public ResponseEntity<List<TransactionResponse>> getTransactionsFromAccountId(@PathVariable Long accountId) {
        return ResponseEntity.status(HttpStatus.OK).body(transactionService.getTransactionsFromAccountId(accountId));
    }
    @GetMapping("/transactions/{transactionType}")
    public ResponseEntity<List<TransactionResponse>> getTransactions(@PathVariable TransactionType transactionType) {
        return ResponseEntity.status(HttpStatus.OK).body(transactionService.getTransactionsByType(transactionType));
    }
    @GetMapping("/transactions/search")
    public Page<TransactionResponse> getTransactionsByFilter(TransactionFilterDto transactionFilterDto,
                                                    @PageableDefault(direction = Sort.Direction.ASC) Pageable pageable) {
        return transactionService.findTransactionByFilter(transactionFilterDto, pageable);
    }
}
