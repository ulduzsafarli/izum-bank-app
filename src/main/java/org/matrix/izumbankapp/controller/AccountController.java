package org.matrix.izumbankapp.controller;

import org.matrix.izumbankapp.enumeration.accounts.AccountStatus;
import org.matrix.izumbankapp.model.accounts.*;
import org.matrix.izumbankapp.model.auth.ResponseDto;
import org.matrix.izumbankapp.service.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user/account")
public class AccountController {
    private final AccountService accountService;

    @GetMapping("/search")
    public Page<AccountResponse> getAccountByFilter(AccountFilterDto accountFilterDto,
                                                    @PageableDefault(direction = Sort.Direction.ASC) Pageable pageable) {
        return accountService.findAccountsByFilter(accountFilterDto, pageable);
    }

    @GetMapping
    public ResponseEntity<List<AccountResponse>> getAllAccounts() {
        return ResponseEntity.status(HttpStatus.OK).body(accountService.getAllAccounts());
    }

    @GetMapping("/accountId")
    public ResponseEntity<AccountResponse> getAccountById(@RequestParam Long accountId) {
        return ResponseEntity.status(HttpStatus.OK).body(accountService.getAccountById(accountId));
    }

    @PostMapping
    public ResponseEntity<AccountResponse> createAccount(@Valid @RequestBody AccountCreateDto account) {
        return ResponseEntity.status(HttpStatus.CREATED).body(accountService.createAccount(account));
    }

    @PutMapping
    public ResponseEntity<ResponseDto> updateAccount(@RequestParam Long accountId, @Valid @RequestBody AccountRequest account) {
        return ResponseEntity.status(HttpStatus.OK).body(accountService.updateAccount(accountId, account));
    }

    @DeleteMapping
    public ResponseEntity<ResponseDto> deleteAccount(@RequestParam Long accountId) {
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(accountService.deleteAccount(accountId));
    }

    @PutMapping("/closure")
    public ResponseEntity<ResponseDto> closeAccount(@RequestParam String accountNumber) {
        return ResponseEntity.status(HttpStatus.OK).body(accountService.closeAccount(accountNumber));
    }

    @PatchMapping("/status")
    public ResponseEntity<ResponseDto> updateAccountStatus(@RequestParam String accountNumber, @RequestParam AccountStatus accountStatusUpdate) {
        return ResponseEntity.status(HttpStatus.OK).body(accountService.updateStatus(accountNumber, accountStatusUpdate));
    }

    @GetMapping("/balance")
    public ResponseEntity<String> getAccountBalance(@RequestParam String accountNumber) {
        return ResponseEntity.status(HttpStatus.OK).body(accountService.getBalance(accountNumber));
    }

}
