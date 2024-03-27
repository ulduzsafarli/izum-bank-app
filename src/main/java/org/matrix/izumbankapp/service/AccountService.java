package org.matrix.izumbankapp.service;

import org.matrix.izumbankapp.model.accounts.*;
import org.matrix.izumbankapp.model.auth.AccountStatusUpdate;
import org.matrix.izumbankapp.model.auth.ResponseDto;
import org.matrix.izumbankapp.model.deposits.DepositRequest;
import org.matrix.izumbankapp.model.users.UserAccountsResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;


public interface AccountService {

    Page<AccountResponse> findAccountsByFilter(AccountFilterDto accountFilterDto, Pageable pageRequest);

    List<AccountResponse> getAllAccounts();

    AccountResponse getAccountById(Long accountId);

    AccountResponse getAccountByAccountNumber(String accountNumber);

    List<AccountsUserResponse> getAllAccountsByUserId(Long userId);

    ResponseDto createAccount(AccountCreateDto account);

    ResponseDto updateAccount(Long accountId, AccountRequest account);

    ResponseDto deleteAccount(Long accountId);

    ResponseDto closeAccount(String accountNumber);

    ResponseDto updateStatus(String accountNumber, AccountStatusUpdate accountStatusUpdate);

    String getBalance(String accountNumber);

    UserAccountsResponse getUserByAccountNumber(String accountNumber);

    ResponseDto transferToAccount(TransferMoneyRequest transferMoneyRequest);

    ResponseDto withdrawal(WithdrawalRequest withdrawalRequest);

    ResponseDto createDepositAccount(DepositRequest depositRequest);

    void saveAccount(AccountResponse account);

    List<AccountResponse> getDepositsCreatedOnDate(int dayOfMonth);
}
