package com.example.mybankapplication.service.impl;

import com.example.mybankapplication.dao.entities.AccountEntity;
import com.example.mybankapplication.exception.DatabaseException;
import com.example.mybankapplication.exception.NotFoundException;
import com.example.mybankapplication.mapper.AccountMapper;
import com.example.mybankapplication.model.accounts.AccountFilterDto;
import com.example.mybankapplication.model.accounts.AccountRequest;
import com.example.mybankapplication.model.accounts.AccountResponse;
import com.example.mybankapplication.dao.repository.AccountRepository;
import com.example.mybankapplication.service.AccountService;
import com.example.mybankapplication.specifications.AccountSpecifications;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountServiceImpl implements AccountService {

    public final AccountRepository accountRepository;
    public final AccountMapper accountMapper;
//    private final UserServiceImpl userService;

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

    public List<AccountResponse> getAllAccounts() {
        log.info("Retrieving all accounts");
        try {
            List<AccountEntity> accountEntityList = accountRepository.findAll();
            List<AccountResponse> accountDtoList = accountEntityList.stream()
                    .map(accountMapper::toDto).toList();
            log.info("Successfully retrieved all accounts");
            return accountDtoList;
        } catch (DataAccessException ex) {
            throw new DatabaseException("Failed to get all accounts from the database", ex);
        }
    }

    public AccountResponse getAccountById(Long accountId) {
        log.info("Retrieving account by ID: {}", accountId);
        try {
            AccountEntity accountEntity = accountRepository.findById(accountId)
                    .orElseThrow(() -> new NotFoundException("Account with ID " + accountId + " not found"));
            AccountResponse accountResponse = accountMapper.toDto(accountEntity);
            log.info("Successfully retrieved account");
            return accountResponse;
        } catch (DataAccessException ex) {
            throw new DatabaseException("Failed to get accounts by ID from the database", ex);
        }
    }

    @Override
    public AccountResponse getAccountByAccountNumber(String accountNumber) {
        log.info("Retrieving account by account number: {}", accountNumber);
        try {
            AccountEntity accountEntity = accountRepository.findByAccountNumber(accountNumber)
                    .orElseThrow(() -> new NotFoundException("Account with accountNumber " + accountNumber + " not found"));
            AccountResponse accountResponse = accountMapper.toDto(accountEntity);
            log.info("Successfully retrieved account");
            return accountResponse;
        } catch (DataAccessException ex) {
            throw new DatabaseException("Failed to get accounts by account number from the database", ex);
        }
    }

//    public List<AccountResponse> getAllAccountsByUserId(Long userId) {
//        log.info("Retrieving all accounts by user ID: {}", userId);
//        try {
//            UserResponse userResponseList = userService.getUserById(userId);
//            List<AccountResponse> accountResponses = userResponseList.getAccounts();
//            log.info("Successfully retrieved all accounts by user ID: {}", userId);
//            return accountResponses;
//        } catch (DataAccessException ex) {
//            throw new DatabaseException("Failed to get accounts by user ID from the database", ex);
//        }
//    }

//    @Transactional
//    public void createAccount(Long userId, AccountRequest account) { //create cif
//        log.info("Creating account for user: {}", userId);
//        try {
//            AccountEntity accountEntity = accountMapper.fromDto(account);
//            String newAccountNumber = generateAccountNumber();
//            accountEntity.setAccountNumber(newAccountNumber);
//            accountEntity.setUser(userMapper.toEntity(userService.readUserById(userId)));
//            accountRepository.save(accountEntity);
//            log.info("Account created successfully");
//        } catch (NotFoundException ex) {
//            throw new NotFoundException("Failed to find user with ID: " + userId);
//        } catch (DataAccessException ex) {
//            throw new DatabaseException("Failed to add new account to the database", ex);
//        }
//    }

    //Добавить кеширование
    public String generateAccountNumber() {
        log.info("Generating unique account number");
        do {
            // Генерация случайного числа в диапазоне от 1000000 до 9999999
            String randomNumber = String.valueOf(ThreadLocalRandom.current().nextInt(1000000, 10000000));
            // Проверка наличия сгенерированного числа в базе данных
            boolean isUnique = !accountRepository.existsByAccountNumber(randomNumber);
            if (isUnique) {
                log.info("Account number generated successfully");
                return randomNumber;
            }
        } while (true);
    }

    @Transactional
    public void updateAccount(Long accountId, AccountRequest account) {
        log.info("Updating account by ID: {}", accountId);
        accountRepository.findById(accountId).ifPresentOrElse(
                accountEntity -> {
                    accountMapper.updateEntityFromDto(account, accountEntity);
                    accountRepository.save(accountEntity);
                    log.info("Account updated successfully");
                },
                () -> {
                    throw new NotFoundException("Account with ID " + accountId + " not found");
                }
        );
    }

    public void deleteAccount(Long accountId) {
        log.info("Deleting account by ID: {}", accountId);
        if (accountRepository.existsById(accountId)) {
            accountRepository.deleteById(accountId);
            log.info("Account deleted successfully");
        } else {
            throw new NotFoundException("Account with ID " + accountId + " not found");
        }
    }

    //yeniden
    @Override
    public AccountResponse readByAccountNumber(Long accountId) {

        return accountRepository.findById(accountId)
                .map(accountMapper::toDto)
                .orElseThrow(() -> new NotFoundException("Account with ID " + accountId + " not found"));
    }

}

