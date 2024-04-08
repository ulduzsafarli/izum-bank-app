package org.matrix.izumbankapp.controller;

import lombok.RequiredArgsConstructor;
import org.matrix.izumbankapp.model.transactions.TransactionFilterDto;
import org.matrix.izumbankapp.model.transactions.TransactionResponse;
import org.matrix.izumbankapp.service.TransactionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/transactions")
public class TransactionController {

    private final TransactionService transactionService;
    @GetMapping("/search")
    public Page<TransactionResponse> getByFilter(TransactionFilterDto transactionFilterDto,
                                                 @PageableDefault(direction = Sort.Direction.ASC) Pageable pageable) {
        return transactionService.findByFilter(transactionFilterDto, pageable);
    }
    @GetMapping("/accounts/{accountId}")
    @ResponseStatus(HttpStatus.OK)
    public List<TransactionResponse> getFromAccountId(@PathVariable Long accountId) {
        return transactionService.getFromAccountId(accountId);
    }
    @GetMapping("/ID/{id}")
    @ResponseStatus(HttpStatus.OK)
    public TransactionResponse getById(@PathVariable Long id) {
        return transactionService.getByID(id);
    }

    @GetMapping("/UUID/{uuid}")
    @ResponseStatus(HttpStatus.OK)
    public TransactionResponse getByUUID(@PathVariable String uuid) {
        return transactionService.getByUUID(uuid);
    }


}
