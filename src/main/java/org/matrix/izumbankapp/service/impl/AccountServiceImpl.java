package org.matrix.izumbankapp.service.impl;

import org.matrix.izumbankapp.dao.entities.AccountEntity;
import org.matrix.izumbankapp.enumeration.accounts.AccountStatus;
import org.matrix.izumbankapp.enumeration.transaction.TransactionStatus;
import org.matrix.izumbankapp.exception.*;
import org.matrix.izumbankapp.exception.accounts.AccountClosingException;
import org.matrix.izumbankapp.exception.accounts.AccountStatusException;
import org.matrix.izumbankapp.exception.transactions.TransactionAmountException;
import org.matrix.izumbankapp.exception.transactions.TransactionLimitException;
import org.matrix.izumbankapp.mapper.AccountMapper;
import org.matrix.izumbankapp.model.accounts.*;
import org.matrix.izumbankapp.dao.repository.AccountRepository;
import org.matrix.izumbankapp.model.auth.AccountStatusUpdate;
import org.matrix.izumbankapp.model.auth.ResponseDto;
import org.matrix.izumbankapp.model.transactions.TransactionAccountRequest;
import org.matrix.izumbankapp.model.transactions.TransactionResponse;
import org.matrix.izumbankapp.model.users.UserAccountsResponse;
import org.matrix.izumbankapp.service.AccountService;
import org.matrix.izumbankapp.service.TransactionService;
import org.matrix.izumbankapp.service.UserService;
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

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountServiceImpl implements AccountService {

//    String errorMessageFormat = "Account with number %s not found";
//new NotFoundException(String.format(errorMessageFormat, toAccountNumber)))

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
    @Transactional(rollbackFor = {TransactionAmountException.class, TransactionLimitException.class})
    public ResponseDto transferMoneyToAccount(TransferMoneyRequest transferMoneyRequest) {
        log.info("Transferring money from {} to {}. Details: {}", transferMoneyRequest.getFromAccountNumber(),
                transferMoneyRequest.getToAccountNumber(), transferMoneyRequest.getTransactionAccountRequest());

        String fromAccountNumber = transferMoneyRequest.getFromAccountNumber();
        String toAccountNumber = transferMoneyRequest.getToAccountNumber();
        TransactionAccountRequest transactionAccountRequest = transferMoneyRequest.getTransactionAccountRequest();

        var fromAccount = accountRepository.findByAccountNumber(fromAccountNumber)
                .orElseThrow(() -> new NotFoundException(WITH_NUMBER_NOT_FOUND + toAccountNumber));
        var toAccount = accountRepository.findByAccountNumber(toAccountNumber)
                .orElseThrow(() -> new NotFoundException(WITH_NUMBER_NOT_FOUND + toAccountNumber));

        if (!verifyPin(fromAccount, transferMoneyRequest.getPin())) {
            throw new InvalidPinException("Invalid PIN provided");
        }

        validateTransaction(fromAccount, transactionAccountRequest);

        fromAccount.setCurrentBalance(fromAccount.getCurrentBalance().subtract(transactionAccountRequest.getAmount()));
        var fromTransaction = transactionService.createTransactionForTransferring(fromAccount.getId(), transactionAccountRequest);
        toAccount.setCurrentBalance(toAccount.getCurrentBalance().add(transactionAccountRequest.getAmount()));
        var toTransaction = transactionService.createTransactionForTransferring(toAccount.getId(), transactionAccountRequest);

        try {
            accountRepository.save(fromAccount);
            accountRepository.save(toAccount);
            updateTransactionsStatus(List.of(fromTransaction, toTransaction), TransactionStatus.SUCCESSFUL);
            return ResponseDto.builder().responseMessage("Successfully transferred money").build();

        } catch (DataAccessException ex) {
            updateTransactionsStatus(List.of(fromTransaction, toTransaction), TransactionStatus.FAILED);
            throw new DatabaseException("Data access error during transfer", ex);
        } catch (Exception ex) {
            updateTransactionsStatus(List.of(fromTransaction, toTransaction), TransactionStatus.FAILED);
            throw new RuntimeException("Unexpected error during transfer", ex);
        }
    }

    private void updateTransactionsStatus(List<TransactionResponse> transactions, TransactionStatus transactionStatus) {
        transactions.forEach(transaction -> transactionService.updateTransactionStatus(transaction.getId(), transactionStatus));
    }

    private void validateTransaction(AccountEntity fromAccount, TransactionAccountRequest transactionAccountRequest) {
        if (!validateTransactionLimit(fromAccount, transactionAccountRequest.getAmount())) {
            throw new TransactionLimitException("Transaction limit exceeded");
        }
        if (!validateSufficientBalance(fromAccount, transactionAccountRequest.getAmount())) {
            throw new TransactionAmountException("Insufficient funds on the sender's account");
        }
        if (!validateAvailableBalanceAfterTransfer(fromAccount, transactionAccountRequest.getAmount())) {
            throw new TransactionAmountException("Exceeding the allowable limit of funds on the recipient's account");
        }
    }

    private boolean verifyPin(AccountEntity account, String pin) {
        log.info("Verify PIN for account number: {} ", account.getAccountNumber());
        boolean isPinValid = passwordEncoder.matches(pin, account.getPin());
        log.info(isPinValid ? "PIN verification successful." : "PIN verification failed.");
        return isPinValid;
    }

    private boolean validateTransactionLimit(AccountEntity account, BigDecimal amount) {
        return account.getTransactionLimit() == null ||
                account.getTransactionLimit().compareTo(amount) >= 0;
    }

    private boolean validateSufficientBalance(AccountEntity account, BigDecimal amount) {
        return account.getCurrentBalance().compareTo(amount) >= 0;
    }

    private boolean validateAvailableBalanceAfterTransfer(AccountEntity account, BigDecimal amount) {
        try {
            return account.getCurrentBalance().add(amount).compareTo(account.getAvailableBalance()) >= 0;
        } catch (ArithmeticException e) {
            log.error("Overflow occurred during balance calculation", e);
            throw new IllegalStateException("Available balance calculation failed");
        }
    }

}

