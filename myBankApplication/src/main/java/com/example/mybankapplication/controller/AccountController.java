package com.example.mybankapplication.controller;

import com.example.mybankapplication.model.accounts.AccountRequest;
import com.example.mybankapplication.model.accounts.AccountCreateDto;
import com.example.mybankapplication.model.accounts.AccountResponse;
import com.example.mybankapplication.model.accounts.AccountFilterDto;
import com.example.mybankapplication.model.auth.AccountStatusUpdate;
import com.example.mybankapplication.model.auth.ResponseDto;
import com.example.mybankapplication.model.transactions.TransactionAccountRequest;
import com.example.mybankapplication.model.transactions.TransactionResponse;
import com.example.mybankapplication.model.users.UserAccountsResponse;
import com.example.mybankapplication.service.AccountService;
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
    public Page<AccountResponse> getAccountByFilter(@Valid AccountFilterDto accountFilterDto,
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

    @GetMapping("/user/{userId}/accounts")
    public ResponseEntity<List<AccountResponse>> getAllAccountsByUserId(@PathVariable Long userId) {
        return ResponseEntity.status(HttpStatus.OK).body(accountService.getAllAccountsByUserId(userId));
    }

    //TODO user_id
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

    //UserController
    @GetMapping("/user/accounts")
    public ResponseEntity<UserAccountsResponse> readUserByAccountNumber(@RequestParam String accountNumber) {
        return ResponseEntity.status(HttpStatus.OK).body(accountService.readUserByAccountId(accountNumber));
    }

    @GetMapping("/accounts/{accountId}/transactions")
    public ResponseEntity<List<TransactionResponse>> getTransactionsFromAccountId(@PathVariable Long accountId) {
        return ResponseEntity.status(HttpStatus.OK).body(accountService.getTransactionsFromAccountId(accountId));
    }

    //TODO if the valute is differ then it convert //TODO pin verification
    @PostMapping("/accounts/{fromAccountNumber}/transfer")
    public ResponseEntity<ResponseDto> transferMoneyToAccount(@PathVariable String fromAccountNumber,
                                                              @RequestParam String toAccountNumber,
                                                              @RequestBody TransactionAccountRequest transactionAccountRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(accountService.transferMoneyToAccount(fromAccountNumber, toAccountNumber, transactionAccountRequest));
    }

}
