package org.matrix.izumbankapp.service.impl;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.matrix.izumbankapp.dao.entities.AccountEntity;
import org.matrix.izumbankapp.enumeration.accounts.AccountStatus;
import org.matrix.izumbankapp.enumeration.accounts.AccountType;
import org.matrix.izumbankapp.enumeration.accounts.CurrencyType;
import org.matrix.izumbankapp.enumeration.transaction.TransactionStatus;
import org.matrix.izumbankapp.enumeration.transaction.TransactionType;
import org.matrix.izumbankapp.exception.DatabaseException;
import org.matrix.izumbankapp.exception.InvalidPinException;
import org.matrix.izumbankapp.exception.NotFoundException;
import org.matrix.izumbankapp.exception.accounts.TransferException;
import org.matrix.izumbankapp.exception.accounts.WithdrawException;
import org.matrix.izumbankapp.exception.transactions.TransactionAmountException;
import org.matrix.izumbankapp.exception.transactions.TransactionLimitException;
import org.matrix.izumbankapp.exception.transactions.TransactionValidationException;
import org.matrix.izumbankapp.model.accounts.AccountCreateDto;
import org.matrix.izumbankapp.model.accounts.TransferMoneyRequest;
import org.matrix.izumbankapp.model.accounts.WithdrawalRequest;
import org.matrix.izumbankapp.model.auth.ResponseDto;
import org.matrix.izumbankapp.model.deposits.DepositRequest;
import org.matrix.izumbankapp.model.deposits.DepositResponse;
import org.matrix.izumbankapp.model.exchange.ExchangeRequestDto;
import org.matrix.izumbankapp.model.transactions.TransactionResponse;
import org.matrix.izumbankapp.service.DepositService;
import org.matrix.izumbankapp.service.ExchangeService;
import org.matrix.izumbankapp.service.OperationService;
import org.matrix.izumbankapp.service.TransactionService;
import org.matrix.izumbankapp.util.GenerateRandom;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
@Service
@Slf4j
@RequiredArgsConstructor
public class OperationServiceImpl implements OperationService {


    private final TransactionService transactionService;
    private final ExchangeService exchangeService;
    private final DepositService depositService;


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
