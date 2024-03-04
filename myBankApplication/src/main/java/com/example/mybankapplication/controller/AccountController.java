package com.example.mybankapplication.controller;

import com.example.mybankapplication.model.accounts.AccountRequest;
import com.example.mybankapplication.model.accounts.AccountResponse;
import com.example.mybankapplication.model.accounts.AccountFilterDto;
import com.example.mybankapplication.model.customers.CustomerResponse;
import com.example.mybankapplication.service.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class AccountController {

    //Only for branches

    private final AccountService accountService;

    @GetMapping("/accounts/search")
    public Page<AccountResponse> getAccountByFilter(@Valid AccountFilterDto accountFilterDto,
                                                    @PageableDefault(direction = Sort.Direction.ASC) Pageable pageable) {
        return accountService.findAccountsByFilter(accountFilterDto, pageable);
    }

    //Убрать
    @GetMapping("/accounts")
    public ResponseEntity<List<AccountResponse>> getAllAccounts() {
        return new ResponseEntity<>(accountService.getAllAccounts(), HttpStatus.OK);
    }

    @GetMapping("/accounts/search/byAccountId/{accountId}")
    public ResponseEntity<AccountResponse> getAccountById(@PathVariable Long accountId) {
        return new ResponseEntity<>(accountService.getAccountById(accountId), HttpStatus.OK);
    }

    @GetMapping("/accounts/search/byAccountNumber{accountNumber}")
    public ResponseEntity<AccountResponse> getAccountByAccountNumber(@PathVariable String accountNumber) {
        return new ResponseEntity<>(accountService.getAccountByAccountNumber(accountNumber), HttpStatus.OK);
    }

    @GetMapping("/customer/{customerId}/accounts")
    public ResponseEntity<List<AccountResponse>> getAllAccountsByCustomerId(@PathVariable Long customerId) {
        return new ResponseEntity<>(accountService.getAllAccountsByCustomerId(customerId), HttpStatus.OK);
    }

    @PostMapping("/customer/{customerId}/accounts")
    public ResponseEntity<AccountResponse> createAccount(@PathVariable Long customerId, @Valid @RequestBody AccountRequest account) {
        accountService.createAccount(customerId, account);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/accounts/{accountId}")
    public ResponseEntity<Void> updateAccount(@PathVariable Long accountId, @Valid @RequestBody AccountRequest account) {
        accountService.updateAccount(accountId, account);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/accounts/{accountId}")
    public ResponseEntity<Void> deleteAccount(@PathVariable Long accountId) {
        accountService.deleteAccount(accountId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
