package com.example.mybankapplication.service;

import com.example.mybankapplication.model.accounts.AccountFilterDto;
import com.example.mybankapplication.model.accounts.AccountRequest;
import com.example.mybankapplication.model.accounts.AccountResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;


public interface AccountService {

    Page<AccountResponse> findAccountsByFilter(AccountFilterDto accountFilterDto, Pageable pageRequest);

    List<AccountResponse> getAllAccounts();

    AccountResponse getAccountById(Long accountId);

    AccountResponse getAccountByAccountNumber(String accountNumber);

    List<AccountResponse> getAllAccountsByCustomerId(Long customerId);

    void createAccount(Long customerId, AccountRequest account);

    void updateAccount(Long accountId, AccountRequest account);

    void deleteAccount(Long accountId);

}
