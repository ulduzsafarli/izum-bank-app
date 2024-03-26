package org.matrix.izumbankapp.controller;

import org.matrix.izumbankapp.model.accounts.*;
import org.matrix.izumbankapp.model.auth.AccountStatusUpdate;
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
@RequestMapping("/api/v1")
public class AccountController {
    private final AccountService accountService;

    @GetMapping("/accounts/search")
    public Page<AccountResponse> getAccountByFilter(AccountFilterDto accountFilterDto,
                                                    @PageableDefault(direction = Sort.Direction.ASC) Pageable pageable) {
        return accountService.findAccountsByFilter(accountFilterDto, pageable);
    }

    @GetMapping("/accounts")
    public ResponseEntity<List<AccountResponse>> getAllAccounts() {
        return ResponseEntity.status(HttpStatus.OK).body(accountService.getAllAccounts());
    }

    @GetMapping("/accounts/accountId")
    public ResponseEntity<AccountResponse> getAccountById(@RequestParam Long accountId) {
        return ResponseEntity.status(HttpStatus.OK).body(accountService.getAccountById(accountId));
    }

    @GetMapping("/accounts/accountNumber")
    public ResponseEntity<AccountResponse> getAccountByAccountNumber(@RequestParam String accountNumber) {
        return ResponseEntity.status(HttpStatus.OK).body(accountService.getAccountByAccountNumber(accountNumber));
    }

    @PostMapping("/user/accounts")
    public ResponseEntity<ResponseDto> createAccount(@Valid @RequestBody AccountCreateDto account) {
        return ResponseEntity.status(HttpStatus.CREATED).body(accountService.createAccount(account));
    }

    @PutMapping("/accounts")
    public ResponseEntity<ResponseDto> updateAccount(@RequestParam Long accountId, @Valid @RequestBody AccountRequest account) {
        return ResponseEntity.status(HttpStatus.OK).body(accountService.updateAccount(accountId, account));
    }

    @DeleteMapping("/accounts")
    public ResponseEntity<ResponseDto> deleteAccount(@RequestParam Long accountId) {
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(accountService.deleteAccount(accountId));
    }

    @PutMapping("/accounts/closure")
    public ResponseEntity<ResponseDto> closeAccount(@RequestParam String accountNumber) {
        return ResponseEntity.status(HttpStatus.OK).body(accountService.closeAccount(accountNumber));
    }

    @PatchMapping("/accounts/status")
    public ResponseEntity<ResponseDto> updateAccountStatus(@RequestParam String accountNumber, @RequestBody AccountStatusUpdate accountStatusUpdate) {
        return ResponseEntity.status(HttpStatus.OK).body(accountService.updateStatus(accountNumber, accountStatusUpdate));
    }

    @GetMapping("/accounts/balance")
    public ResponseEntity<String> getAccountBalance(@RequestParam String accountNumber) {
        return ResponseEntity.status(HttpStatus.OK).body(accountService.getBalance(accountNumber));
    }

    @PostMapping("/accounts/transfer")
    public ResponseEntity<ResponseDto> transferToAccount(@Valid @RequestBody TransferMoneyRequest transferMoneyRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(accountService.transferMoneyToAccount(transferMoneyRequest));
    }

    @PostMapping("/accounts/withdrawal")
    public ResponseEntity<ResponseDto> withdrawal(@Valid @RequestBody WithdrawalRequest withdrawalRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(accountService.withdrawal(withdrawalRequest));
    }

    @PostMapping("/accounts/deposit")
    public ResponseEntity<ResponseDto> deposit(@Valid @RequestBody WithdrawalRequest withdrawalRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(accountService.withdrawal(withdrawalRequest));
    }



}
