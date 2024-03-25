package com.example.mybankapplication.service.impl;

import com.example.mybankapplication.dao.entities.AccountEntity;
import com.example.mybankapplication.enumeration.accounts.AccountStatus;
import com.example.mybankapplication.enumeration.transaction.TransactionStatus;
import com.example.mybankapplication.exception.*;
import com.example.mybankapplication.mapper.AccountMapper;
import com.example.mybankapplication.model.accounts.*;
import com.example.mybankapplication.dao.repository.AccountRepository;
import com.example.mybankapplication.model.auth.AccountStatusUpdate;
import com.example.mybankapplication.model.auth.ResponseDto;
import com.example.mybankapplication.model.transactions.TransactionAccountRequest;
import com.example.mybankapplication.model.transactions.TransactionResponse;
import com.example.mybankapplication.model.users.UserAccountsResponse;
import com.example.mybankapplication.service.AccountService;
import com.example.mybankapplication.service.TransactionService;
import com.example.mybankapplication.service.UserService;
import com.example.mybankapplication.util.GenerateRandom;
import com.example.mybankapplication.util.specifications.AccountSpecifications;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountServiceImpl implements AccountService {

    private static final String WITH_ID_NOT_FOUND = "Account with ID not found: ";
    private static final String WITH_NUMBER_NOT_FOUND = "Account with number not found: ";


    private final PasswordEncoder passwordEncoder;
    public final AccountRepository accountRepository;
    public final AccountMapper accountMapper;
    private final UserService userService;
    private final TransactionService transactionService;

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
            var accountDtoList = accountRepository.findAll().stream().map(accountMapper::toDto).toList();
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
                    .orElseThrow(() -> new NotFoundException(WITH_ID_NOT_FOUND + accountId));
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
                    .orElseThrow(() -> new NotFoundException(WITH_NUMBER_NOT_FOUND + accountNumber));
            AccountResponse accountResponse = accountMapper.toDto(accountEntity);
            log.info("Successfully retrieved account");
            return accountResponse;
        } catch (DataAccessException ex) {
            throw new DatabaseException("Failed to get accounts by account number from the database", ex);
        }
    }

    //Добавить кеширование
    @Override
    public List<AccountResponse> getAllAccountsByUserId(Long userId) {
        log.info("Retrieving all accounts by user ID: {}", userId);
        try {
            UserAccountsResponse userResponseList = userService.getUserByIdForAccount(userId);
            List<AccountResponse> accountResponses = userResponseList.getAccounts();
            log.info("Successfully retrieved all accounts by user ID: {}", userId);
            return accountResponses;
        } catch (DataAccessException ex) {
            throw new DatabaseException("Failed to get accounts by user ID from the database", ex);
        }
    }

    @Override
    public ResponseDto createAccount(AccountCreateDto account) {
        log.info("Creating account for user: {}", account.getUserId());
        try {
            AccountEntity accountEntity = accountMapper.fromRequestDtoForUser(account);
            accountEntity.setPin(passwordEncoder.encode(account.getPin()));
            accountEntity.setAccountNumber(GenerateRandom.generateAccountNumber());
            userService.createCif(account.getUserId());
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
                },
                () -> {
                    throw new NotFoundException(WITH_ID_NOT_FOUND + accountId);
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
            throw new NotFoundException(WITH_ID_NOT_FOUND + accountId);
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
                }).orElseThrow(() -> new NotFoundException(WITH_NUMBER_NOT_FOUND + accountNumber));
    }

    @Override
    public String getBalance(String accountNumber) {
        log.info("Getting current balance from account {}", accountNumber);
        AccountEntity account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new NotFoundException(WITH_NUMBER_NOT_FOUND + accountNumber));
        log.info("Get current balance from account {} successfully", accountNumber);
        return account.getAvailableBalance().toString();
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
                }).orElseThrow(() -> new NotFoundException("Account not on the server"));

    }

    //yeniden
    @Override
    public AccountResponse getByAccountNumber(Long accountId) {
        log.info("Closing account: {}", accountId);

        return accountRepository.findById(accountId)
                .map(accountMapper::toDto)
                .orElseThrow(() -> new NotFoundException(WITH_ID_NOT_FOUND + accountId));
    }

    @Override
    public UserAccountsResponse readUserByAccountId(String accountNumber) {
        log.info("Reading user by account number {}", accountNumber);

        try {
            var userAccountsDto = accountRepository.findByAccountNumber(accountNumber)
                    .map(AccountEntity::getUser)
                    .map(userEntity -> userService.getUserByIdForAccount(userEntity.getId()))
                    .orElseThrow(() -> new NotFoundException(WITH_NUMBER_NOT_FOUND + accountNumber));
            log.info("Read user by account number {} successfully", accountNumber);
            return userAccountsDto;
        } catch (DataAccessException ex) {
            throw new DatabaseException("Failed to read user by account number", ex);
        }
    }

    @Override
    public List<TransactionResponse> getTransactionsFromAccountId(Long accountId) {
        log.info("Retrieving transactions for account ID {}", accountId);

        var account = accountRepository.findById(accountId)
                .map(accountMapper::toDto)
                .orElseThrow(() -> new NotFoundException(WITH_ID_NOT_FOUND + accountId))
                .getTransactionResponseList();
        log.info("Successfully retrieved transactions for account ID {}", accountId);
        return account;
    }

    @Override
    @Transactional(rollbackFor = {TransactionAmountException.class, TransactionLimitException.class})
    public ResponseDto transferMoneyToAccount(TransferDto transferDto, TransactionAccountRequest transactionAccountRequest) {
        log.info("Transfer money from account number {} to account {}, details: {}", transferDto.getFromAccountNumber(),
                transferDto.getToAccountNumber(), transactionAccountRequest);

        var fromAccountNumber = transferDto.getFromAccountNumber();
        var toAccountNumber = transferDto.getToAccountNumber();

        // Получение информации о счете, с которого происходит перевод
        var fromAccount = accountRepository.findByAccountNumber(fromAccountNumber)
                .map(accountMapper::toDto)
                .orElseThrow(() -> new NotFoundException(WITH_NUMBER_NOT_FOUND + fromAccountNumber));

        // Проверка лимита на транзакцию
        if (fromAccount.getTransactionLimit() == null ||
                fromAccount.getTransactionLimit().compareTo(transactionAccountRequest.getAmount()) >= 0) {

            // Проверка достаточного баланса для перевода
            if (fromAccount.getCurrentBalance().compareTo(transactionAccountRequest.getAmount()) >= 0) {

                // Обновление баланса счета отправителя
                fromAccount.setCurrentBalance(fromAccount.getCurrentBalance().subtract(transactionAccountRequest.getAmount()));

                // Создание транзакции для счета отправителя
                var fromTransaction = transactionService.createTransactionForTransferring(fromAccountNumber, transactionAccountRequest);

                // Получение информации о счете получателе
                var toAccount = getAccountByAccountNumber(toAccountNumber);

                // Проверка на возможность проведения транзакции на счет получателя
                if (toAccount.getCurrentBalance().add(transactionAccountRequest.getAmount()).compareTo(toAccount.getAvailableBalance()) <= 0) {

                    // Обновление баланса счета получателя
                    toAccount.setCurrentBalance(toAccount.getCurrentBalance().add(transactionAccountRequest.getAmount()));

                    // Создание транзакции для счета получателя
                    var toTransaction = transactionService.createTransactionForTransferring(toAccountNumber, transactionAccountRequest);

                    // Сохранение изменений по счету получателя и обновление статуса транзакции
                    accountRepository.save(accountMapper.fromResponseDto(toAccount));
                    transactionService.updateTransactionStatus(toTransaction.getId(), TransactionStatus.SUCCESSFUL);

                    // Обновление статуса транзакции по счету отправителя и сохранение изменений
                    transactionService.updateTransactionStatus(fromTransaction.getId(), TransactionStatus.SUCCESSFUL);
                    accountRepository.save(accountMapper.fromResponseDto(fromAccount));

                    return ResponseDto.builder().responseMessage("Successfully transfer money").build();
                } else {
                    throw new TransactionAmountException("Insufficient funds on the recipient's account");
                }
            } else {
                throw new TransactionAmountException("Insufficient funds on the sender's account");
            }
        } else {
            throw new TransactionLimitException("Transaction limit exceeded");
        }
    }


}

