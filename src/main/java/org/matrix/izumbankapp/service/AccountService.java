package org.matrix.izumbankapp.service;

import org.matrix.izumbankapp.exception.accounts.InsufficientFundsException;
import org.matrix.izumbankapp.model.accounts.*;
import org.matrix.izumbankapp.model.accounts.AccountStatusUpdate;
import org.matrix.izumbankapp.model.auth.ResponseDto;
import org.matrix.izumbankapp.model.users.UserAccountsResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;


public interface AccountService {

    Page<AccountResponse> findAccountsByFilter(AccountFilterDto accountFilterDto, Pageable pageRequest);

    List<AccountResponse> getAllAccounts();

    AccountResponse getAccountById(Long accountId);

    AccountResponse getAccountByAccountNumber(String accountNumber);

    AccountResponse createAccount(AccountCreateDto account);

    ResponseDto updateAccount(Long accountId, AccountRequest account);

    ResponseDto deleteAccount(Long accountId);

    ResponseDto closeAccount(String accountNumber);

    ResponseDto updateStatus(String accountNumber, AccountStatusUpdate accountStatusUpdate);

    UserAccountsResponse getUserByAccountNumber(String accountNumber);

    String getBalance(String accountNumber);

    void saveAccount(AccountResponse account);

    void validatePin(AccountResponse account, String pin);

    List<AccountResponse> getDepositAccountsCreatedOnDate(int dayOfMonth);

    void updateBalance(String accountNumber, BigDecimal subtract) throws InsufficientFundsException;
}
