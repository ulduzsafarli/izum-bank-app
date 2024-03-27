package org.matrix.izumbankapp.service.impl;

import org.matrix.izumbankapp.dao.entities.AccountEntity;
import org.matrix.izumbankapp.enumeration.accounts.AccountStatus;
import org.matrix.izumbankapp.enumeration.accounts.AccountType;
import org.matrix.izumbankapp.enumeration.accounts.CurrencyType;
import org.matrix.izumbankapp.enumeration.transaction.TransactionStatus;
import org.matrix.izumbankapp.enumeration.transaction.TransactionType;
import org.matrix.izumbankapp.exception.*;
import org.matrix.izumbankapp.exception.accounts.*;
import org.matrix.izumbankapp.exception.transactions.TransactionAmountException;
import org.matrix.izumbankapp.exception.transactions.TransactionLimitException;
import org.matrix.izumbankapp.exception.transactions.TransactionValidationException;
import org.matrix.izumbankapp.mapper.AccountMapper;
import org.matrix.izumbankapp.model.accounts.*;
import org.matrix.izumbankapp.dao.repository.AccountRepository;
import org.matrix.izumbankapp.model.auth.AccountStatusUpdate;
import org.matrix.izumbankapp.model.auth.ResponseDto;
import org.matrix.izumbankapp.model.deposits.DepositRequest;
import org.matrix.izumbankapp.model.deposits.DepositResponse;
import org.matrix.izumbankapp.model.exchange.ExchangeRequestDto;
import org.matrix.izumbankapp.model.transactions.TransactionResponse;
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
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.EntityManager;

import java.time.LocalDate;
import java.util.List;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Period;
import java.util.ArrayList;


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
    private final TransactionService transactionService;
    private final ExchangeService exchangeService;
    private final DepositService depositService;
    private final EntityManager entityManager;

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
    public AccountResponse getAccountByAccountNumber(String accountNumber) {
        log.info("Retrieving account by account number: {}", accountNumber);
        try {
            AccountEntity accountEntity = accountRepository.findByAccountNumber(accountNumber)
                    .orElseThrow(() -> new NotFoundException(String.format(WITH_NUMBER_NOT_FOUND, accountNumber)));
            AccountResponse accountResponse = accountMapper.toDto(accountEntity);
            log.info("Successfully retrieved account");
            return accountResponse;
        } catch (DataAccessException ex) {
            throw new DatabaseException("Failed to get accounts by account number from the database", ex);
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


    //Добавить кеширование
    @Override
    public List<AccountsUserResponse> getAllAccountsByUserId(Long userId) {
        log.info("Retrieving all accounts by user ID: {}", userId);
        try {
            UserAccountsResponse userResponseList = userService.getUserByIdForAccount(userId);
            List<AccountsUserResponse> accountResponses = userResponseList.getAccounts();
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
                },
                () -> {
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

    @Override
    @Transactional(rollbackFor = {TransactionAmountException.class, TransactionLimitException.class})
    public ResponseDto transferToAccount(TransferMoneyRequest transferMoneyRequest) {

        log.info("Transferring money from {} to {}. Details: {}",
                transferMoneyRequest.getFromAccountNumber(),
                transferMoneyRequest.getToAccountNumber(),
                transferMoneyRequest.getTransactionAccountRequest());

        var fromAccount = findAccountOrFail(transferMoneyRequest.getFromAccountNumber());
        var toAccount = findAccountOrFail(transferMoneyRequest.getToAccountNumber());

        validatePin(fromAccount, transferMoneyRequest.getPin());

        BigDecimal transferAmount = transferMoneyRequest.getTransactionAccountRequest().getAmount();
        transferAmount = performCurrencyExchangeIfNeeded(fromAccount, transferAmount, toAccount.getCurrencyType());
        validateTransaction(fromAccount, transferAmount);

        fromAccount.debitBalance(transferAmount);
        transferMoneyRequest.getTransactionAccountRequest().setAmount(transferAmount);
        var fromTransaction = transactionService.createTransaction(fromAccount.getId(),
                transferMoneyRequest.getTransactionAccountRequest(), TransactionType.TRANSFER);

        toAccount.creditBalance(transferAmount);
        var toTransaction = transactionService.createTransaction(toAccount.getId(),
                transferMoneyRequest.getTransactionAccountRequest(), TransactionType.TRANSFER);

        return executeTransfer(fromAccount, toAccount, fromTransaction, toTransaction);
    }

    @Override
    @Transactional
    public ResponseDto createDepositAccount(DepositRequest depositRequest) {
        log.info("Creating deposit account for user: {}", depositRequest.getUserId());

        AccountCreateDto accountCreateDto = AccountCreateDto.builder()
                .accountType(AccountType.DEPOSIT)
                .accountExpireDate(depositRequest.getDepositExpireDate())
                .availableBalance(calculateInterest(depositRequest.getAmount(), depositRequest.getInterest(),
                        depositRequest.getDepositExpireDate()))
                .branchCode("333") //TODO yml
                .currencyType(depositRequest.getCurrencyType())
                .currentBalance(BigDecimal.ZERO)
                .pin(passwordEncoder.encode(depositRequest.getPin()))
                .status(AccountStatus.ACTIVE)
                .userId(depositRequest.getUserId())
                .build();

        userService.createCif(depositRequest.getUserId());
        AccountEntity accountEntity = accountMapper.fromRequestDtoForUser(accountCreateDto);
        accountEntity.setPin(passwordEncoder.encode(depositRequest.getPin()));
        accountEntity.setAccountNumber(GenerateRandom.generateAccountNumber());
        accountRepository.save(accountEntity);

        DepositResponse depositResponse = DepositResponse.builder()
                .account(accountMapper.toDto(accountEntity))
                .amount(depositRequest.getAmount())
                .interestRate(depositRequest.getInterest())
                .yearlyInterest(depositRequest.isYearlyInterest()).build();

        depositService.saveDeposit(depositResponse);

        log.info("Deposit account created successfully");
        return ResponseDto.builder().responseMessage("Successfully created a deposit account").build();
    }

    @Override
    public void saveAccount(AccountResponse account) {
        log.info("Saving account {}", account);
        accountRepository.save(accountMapper.fromResponseDto(account));
        log.info("Successfully save account {}", account);
    }

    @Override
    @Transactional
    public List<AccountResponse> getDepositsCreatedOnDate(int dayOfMonth) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<AccountEntity> criteriaQuery = criteriaBuilder.createQuery(AccountEntity.class);
        Root<AccountEntity> root = criteriaQuery.from(AccountEntity.class);

        criteriaQuery.select(root)
                .where(
                        criteriaBuilder.and(
                                criteriaBuilder.equal(root.get("accountType"), AccountType.DEPOSIT),
                                criteriaBuilder.equal(root.get("status"), AccountStatus.ACTIVE),
                                criteriaBuilder.greaterThan(root.get("accountExpireDate"), LocalDate.now()),
                                criteriaBuilder.equal(criteriaBuilder.function("day", Integer.class, root.get("createdDate")), dayOfMonth)
                        )
                );

        List<AccountEntity> accounts = entityManager.createQuery(criteriaQuery).getResultList();

        return accounts.stream().map(accountMapper::toDto).toList();
    }

    private BigDecimal calculateInterest(BigDecimal amount, BigDecimal interest, LocalDate depositExpireDate) {
        LocalDate currentDate = LocalDate.now();
        Period period = Period.between(currentDate, depositExpireDate);
        int months = period.getMonths();

        // Рассчитываем проценты
        BigDecimal interestRate = BigDecimal.ONE.add(interest.divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP));
        BigDecimal calculatedInterest = amount.multiply(interestRate.pow(months));

        return calculatedInterest.subtract(amount);
    }


    @Override
    public ResponseDto withdrawal(WithdrawalRequest withdrawalRequest) {
        log.info("Withdrawals from {}. Details: {}",
                withdrawalRequest.getFromAccountNumber(),
                withdrawalRequest.getTransactionAccountRequest());

        var fromAccount = findAccountOrFail(withdrawalRequest.getFromAccountNumber());

        validatePin(fromAccount, withdrawalRequest.getPin());

        BigDecimal withdrawAmount = withdrawalRequest.getTransactionAccountRequest().getAmount();
        withdrawAmount = performCurrencyExchangeIfNeeded(fromAccount, withdrawAmount, withdrawalRequest.getCurrencyType());
        validateTransaction(fromAccount, withdrawAmount);
        fromAccount.debitBalance(withdrawAmount);
        withdrawalRequest.getTransactionAccountRequest().setAmount(withdrawAmount);
        var fromTransaction = transactionService.createTransaction(fromAccount.getId(),
                withdrawalRequest.getTransactionAccountRequest(), TransactionType.WITHDRAWAL);
        try {
            accountRepository.save(fromAccount);
            return ResponseDto.builder().responseMessage("Successfully withdraw money").build();
        } catch (DataAccessException ex) {
            updateTransactionsStatus(List.of(fromTransaction), TransactionStatus.FAILED);
            throw new DatabaseException("Data access error during transfer", ex);
        } catch (Exception ex) {
            updateTransactionsStatus(List.of(fromTransaction), TransactionStatus.FAILED);
            throw new WithdrawException("Unexpected error during transfer", ex);
        }
    }

    private BigDecimal performCurrencyExchangeIfNeeded(AccountEntity fromAccount, BigDecimal amount, CurrencyType toCurrency) {
        if (fromAccount.getCurrencyType() != toCurrency) {
            return performCurrencyExchange(fromAccount, amount, toCurrency);
        } else {
            return amount;
        }
    }

    private AccountEntity findAccountOrFail(String accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new NotFoundException(String.format(WITH_NUMBER_NOT_FOUND, accountNumber)));
    }

    private BigDecimal performCurrencyExchange(AccountEntity fromAccount, BigDecimal amount, CurrencyType toCurrency) {
        if (fromAccount.getCurrencyType() == CurrencyType.AZN) {
            return BigDecimal.valueOf(exchangeService.performExchangeFromAZN(
                    ExchangeRequestDto.builder().amount(amount.doubleValue()).currencyType(toCurrency).build()).getConvertedAmount());
        } else {
            return BigDecimal.valueOf(exchangeService.performExchangeToAZN(
                    ExchangeRequestDto.builder().amount(amount.doubleValue()).currencyType(fromAccount.getCurrencyType()).build()).getConvertedAmount());
        }
    }

    private void updateTransactionsStatus(List<TransactionResponse> transactions, TransactionStatus transactionStatus) {
        transactions.forEach(transaction -> transactionService.updateTransactionStatus(transaction.getId(), transactionStatus));
    }

    private ResponseDto executeTransfer(AccountEntity fromAccount, AccountEntity toAccount,
                                        TransactionResponse fromTransaction, TransactionResponse toTransaction) {
        try {
            accountRepository.saveAll(List.of(fromAccount, toAccount));
            updateTransactionsStatus(List.of(fromTransaction, toTransaction), TransactionStatus.SUCCESSFUL);
            return ResponseDto.builder().responseMessage("Successfully transferred money").build();
        } catch (DataAccessException ex) {
            updateTransactionsStatus(List.of(fromTransaction, toTransaction), TransactionStatus.FAILED);
            throw new DatabaseException("Data access error during transfer", ex);
        } catch (Exception ex) {
            updateTransactionsStatus(List.of(fromTransaction, toTransaction), TransactionStatus.FAILED);
            throw new TransferException("Unexpected error during transfer", ex);
        }
    }

    private void validateTransaction(AccountEntity fromAccount, BigDecimal amount) {
        List<String> errors = new ArrayList<>();
        if (!validateTransactionLimit(fromAccount, amount)) {
            errors.add("Transaction limit exceeded");
        }
        if (!validateSufficientBalance(fromAccount, amount)) {
            errors.add("Insufficient funds on the sender's account");
        }
        if (!validateAvailableBalanceAfterTransfer(fromAccount, amount)) {
            errors.add("Exceeding the allowable limit of funds on the recipient's account");
        }
        if (!errors.isEmpty()) {
            throw new TransactionValidationException(String.join("; ", errors));
        }
    }


    private void validatePin(AccountEntity account, String pin) throws InvalidPinException {
        log.info("Validating PIN for account number: {}", account.getAccountNumber());
        boolean isPinValid = passwordEncoder.matches(pin, account.getPin());
        log.info(isPinValid ? "PIN verification successful." : "PIN verification failed.");
        if (!isPinValid) {
            throw new InvalidPinException("Invalid PIN provided");
        }
    }

    private boolean validateTransactionLimit(AccountEntity account, BigDecimal amount) {
        return account.getTransactionLimit() == null ||
                account.getTransactionLimit().compareTo(amount) >= 0;
    }

    private boolean validateSufficientBalance(AccountEntity account, BigDecimal amount) {
        return account.getCurrentBalance().compareTo(amount) >= 0;
    }

    private boolean validateAvailableBalanceAfterTransfer(AccountEntity account, BigDecimal amount) {
        return account.getCurrentBalance().add(amount).compareTo(account.getAvailableBalance()) <= 0;

    }


}

