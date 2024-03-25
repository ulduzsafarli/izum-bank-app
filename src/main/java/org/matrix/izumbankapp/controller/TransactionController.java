package org.matrix.izumbankapp.controller;

import lombok.RequiredArgsConstructor;
import org.matrix.izumbankapp.model.transactions.TransactionResponse;
import org.matrix.izumbankapp.service.TransactionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class TransactionController {
    TransactionService transactionService;
    @GetMapping("/accounts/{accountId}/transactions")
    public ResponseEntity<List<TransactionResponse>> getTransactionsFromAccountId(@PathVariable Long accountId) {
        return ResponseEntity.status(HttpStatus.OK).body(transactionService.getTransactionsFromAccountId(accountId));
    }
}
