package org.matrix.izumbankapp.service.impl;

import org.matrix.izumbankapp.dao.entities.AccountEntity;
import org.matrix.izumbankapp.enumeration.accounts.AccountStatus;
import org.matrix.izumbankapp.enumeration.accounts.AccountType;
import org.matrix.izumbankapp.exception.*;
import org.matrix.izumbankapp.exception.accounts.*;
import org.matrix.izumbankapp.mapper.AccountMapper;
import org.matrix.izumbankapp.model.accounts.*;
import org.matrix.izumbankapp.dao.repository.AccountRepository;
import org.matrix.izumbankapp.model.auth.AccountStatusUpdate;
import org.matrix.izumbankapp.model.auth.ResponseDto;
import org.matrix.izumbankapp.model.users.UserAccountsResponse;
import org.matrix.izumbankapp.service.*;
import org.matrix.izumbankapp.util.GenerateRandom;
import org.matrix.izumbankapp.util.specifications.AccountSpecifications;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import java.math.BigDecimal;


@Service
@RequiredArgsConstructor
@Slf4j
public class AccountServiceImpl implements AccountService {

    private static final String NOT_FOUND = "Account not found";
    private static final String WITH_ID_NOT_FOUND = "Account with ID %s not found: ";
    private static final String WITH_NUMBER_NOT_FOUND = "Account with number %s not found";


    private final PasswordEncoder passwordEncoder;
    public final AccountRepository accountRepository;
    public final AccountMapper accountMapper;
    private final UserService userService;


    @Override
    public Page<AccountResponse> findAccountsByFilter(AccountFilterDto accountFilterDto, Pageable pageRequest) {
        log.info("Searching accounts by filter: {}", accountFilterDto);
        try {
            Specification<AccountEntity> accountSpecification = AccountSpecifications.getAccountSpecification(accountFilterDto);
            Page<AccountEntity> accountEntityPage = accountRepository.findAll(accountSpecification, pageRequest);
            log.info("Successfully found accounts");
            return accountEntityPage.map(accountMapper::toDto);
        } catch (DataAccessException ex) {
            throw new DatabaseException("Failed to find an account in the database", ex);
        }
    }

    @Override
    public AccountResponse getAccountById(Long accountId) {
        log.info("Retrieving account by ID: {}", accountId);
        try {
            AccountEntity accountEntity = accountRepository.findById(accountId)
                    .orElseThrow(() -> new NotFoundException(String.format(WITH_ID_NOT_FOUND, accountId)));
            AccountResponse accountResponse = accountMapper.toDto(accountEntity);
            log.info("Successfully retrieved account");
            return accountResponse;
        } catch (DataAccessException ex) {
            throw new DatabaseException("Failed to get accounts by ID from the database", ex);
        }
    }

    @Override
    public List<AccountResponse> getAllAccounts() {
        log.info("Retrieving all accounts");
        try {
            var accountDtoList = accountRepository.findAll().stream().map(accountMapper::toDto).toList();
            log.info("Successfully retrieved all accounts");
            return accountDtoList;
        } catch (DataAccessException ex) {
            throw new DatabaseException("Failed to get all accounts from the database", ex);
        }
    }

    @Override
    public ResponseDto createAccount(AccountCreateDto account) {
        log.info("Creating account for user: {}", account.getUserId());
        try {
            userService.createCif(account.getUserId());
            AccountEntity accountEntity = accountMapper.fromRequestDtoForUser(account);
            accountEntity.setPin(passwordEncoder.encode(account.getPin()));
            accountEntity.setAccountNumber(GenerateRandom.generateAccountNumber());
            accountRepository.save(accountEntity);
            log.info("Account created successfully");
            return ResponseDto.builder().responseMessage("Account created successfully").build();
        } catch (NotFoundException ex) {
            throw new NotFoundException("Failed to find user with ID: " + account.getUserId());
        } catch (DataAccessException ex) {
            throw new DatabaseException("Failed to add new account to the database", ex);
        }
    }


    @Override
    @Transactional
    public ResponseDto updateAccount(Long accountId, AccountRequest account) {
        log.info("Updating account by ID: {}", accountId);
        accountRepository.findById(accountId).ifPresentOrElse(
                accountEntity -> {
                    accountMapper.updateEntityFromDto(account, accountEntity);
                    accountRepository.save(accountEntity);
                    log.info("Successfully updated account {}", account);
                }, () -> {
                    throw new NotFoundException(String.format(WITH_ID_NOT_FOUND, accountId));
                }
        );
        return ResponseDto.builder()
                .responseMessage("Account updated successfully").build();
    }

    @Override
    @Transactional
    public ResponseDto deleteAccount(Long accountId) {
        log.info("Deleting account by ID: {}", accountId);
        if (accountRepository.existsById(accountId)) {
            accountRepository.deleteById(accountId);
            log.info("Account deleted successfully");
            return ResponseDto.builder().responseMessage("Account deleted successfully").build();
        } else {
            throw new NotFoundException(String.format(WITH_ID_NOT_FOUND, accountId));
        }
    }

    @Override
    public ResponseDto closeAccount(String accountNumber) {
        log.info("Closing account: {}", accountNumber);
        return accountRepository.findByAccountNumber(accountNumber)
                .map(account -> {
                    if (BigDecimal.valueOf(Double.parseDouble(getBalance(accountNumber))).compareTo(BigDecimal.ZERO) != 0) {
                        throw new AccountClosingException("Balance should be zero");
                    }
                    account.setStatus(AccountStatus.CLOSED);
                    return ResponseDto.builder().responseMessage("Account closed successfully").build();
                }).orElseThrow(() -> new NotFoundException(String.format(WITH_NUMBER_NOT_FOUND, accountNumber)));
    }

    @Override
    public String getBalance(String accountNumber) {
        log.info("Getting current balance from account {}", accountNumber);
        AccountEntity account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new NotFoundException(String.format(WITH_NUMBER_NOT_FOUND, accountNumber)));
        log.info("Get current balance from account {} successfully", accountNumber);
        return account.getCurrentBalance().toString();
    }


    @Override
    public ResponseDto updateStatus(String accountNumber, AccountStatusUpdate accountUpdate) {
        log.info("Updating status for account {}", accountNumber);
        return accountRepository.findByAccountNumber(accountNumber)
                .map(account -> {
                    if (account.getStatus().equals(accountUpdate.getAccountStatus())) {
                        throw new AccountStatusException("Account is already " + account.getStatus());
                    }
                    account.setStatus(accountUpdate.getAccountStatus());
                    accountRepository.save(account);
                    return ResponseDto.builder().responseMessage("Account updated successfully").build();
                }).orElseThrow(() -> new NotFoundException(NOT_FOUND));
    }

    @Override
    public UserAccountsResponse getUserByAccountNumber(String accountNumber) {
        log.info("Reading user by account number {}", accountNumber);

        try {
            var userAccountsDto = accountRepository.findByAccountNumber(accountNumber)
                    .map(AccountEntity::getUser)
                    .map(userEntity -> userService.getUserByIdForAccount(userEntity.getId()))
                    .orElseThrow(() -> new NotFoundException(String.format(WITH_NUMBER_NOT_FOUND, accountNumber)));
            log.info("Read user by account number {} successfully", accountNumber);
            return userAccountsDto;
        } catch (DataAccessException ex) {
            throw new DatabaseException("Failed to read user by account number", ex);
        }
    }

    //TODO silmek olar
    @Override
    public void saveAccount(AccountResponse account) {
        log.info("Saving account {}", account);
        accountRepository.save(accountMapper.fromResponseDto(account));
        log.info("Successfully save account {}", account);
    }

    //TODO duzelis
    @Override
    @Transactional
    public List<AccountResponse> getDepositAccountsCreatedOnDate(int dayOfMonth) {
        List<AccountEntity> accountEntities = accountRepository.findAccountsByDateAndTypeAndStatus(
                AccountType.DEPOSIT, AccountStatus.ACTIVE, LocalDate.now(), dayOfMonth);

        return accountEntities.stream().map(accountMapper::toDto).toList();
    }


}