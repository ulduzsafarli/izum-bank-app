package com.example.mybankapplication.service;

import com.example.mybankapplication.model.accounts.AccountFilterDto;
import com.example.mybankapplication.model.accounts.AccountRequest;
import com.example.mybankapplication.model.accounts.AccountResponse;
import com.example.mybankapplication.model.auth.AccountStatusUpdate;
import com.example.mybankapplication.model.auth.ResponseDto;
import com.example.mybankapplication.model.users.UserAccountsDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;


public interface AccountService {

    Page<AccountResponse> findAccountsByFilter(AccountFilterDto accountFilterDto, Pageable pageRequest);

    List<AccountResponse> getAllAccounts();

    AccountResponse getAccountById(Long accountId);

    AccountResponse getAccountByAccountNumber(String accountNumber);

    List<AccountResponse> getAllAccountsByUserId(Long userId);

    ResponseDto createAccount(Long userId, AccountRequest account);

    ResponseDto updateAccount(Long accountId, AccountRequest account);

    ResponseDto deleteAccount(Long accountId);

    AccountResponse getByAccountNumber(Long accountId);

    ResponseDto closeAccount(String accountNumber);

    ResponseDto updateStatus(String accountNumber, AccountStatusUpdate accountStatusUpdate);

    String getBalance(String accountNumber);

    UserAccountsDto readUserByAccountId(String accountNumber);
}
