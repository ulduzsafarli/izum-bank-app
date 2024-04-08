package org.matrix.izumbankapp.service;

import org.matrix.izumbankapp.enumeration.accounts.AccountStatus;
import org.matrix.izumbankapp.exception.accounts.InsufficientFundsException;
import org.matrix.izumbankapp.model.accounts.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public interface AccountService {

    Page<AccountResponse> findByFilter(AccountFilterDto accountFilterDto, Pageable pageRequest);

    AccountResponse getById(Long accountId);

    AccountResponse getByAccountNumber(String accountNumber);

    AccountResponse create(AccountCreateDto account);

    AccountResponse update(Long accountId, AccountRequest account);

    void delete(Long accountId);

    void updateStatus(String accountNumber, AccountStatus accountStatusUpdate);

    void validatePin(AccountResponse account, String pin);

    void updateBalance(String accountNumber, BigDecimal subtract) throws InsufficientFundsException;
}
