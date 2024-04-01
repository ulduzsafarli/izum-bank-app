package org.matrix.izumbankapp.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.matrix.izumbankapp.enumeration.accounts.*;
import org.matrix.izumbankapp.enumeration.transaction.*;
import org.matrix.izumbankapp.exception.accounts.InsufficientFundsException;
import org.matrix.izumbankapp.exception.accounts.TransferException;
import org.matrix.izumbankapp.exception.transactions.*;
import org.matrix.izumbankapp.model.accounts.AccountCreateDto;
import org.matrix.izumbankapp.model.accounts.AccountResponse;
import org.matrix.izumbankapp.model.accounts.TransferMoneyRequest;
import org.matrix.izumbankapp.model.accounts.WithdrawalRequest;
import org.matrix.izumbankapp.model.auth.ResponseDto;
import org.matrix.izumbankapp.model.deposits.*;
import org.matrix.izumbankapp.model.exchange.ExchangeRequestDto;
import org.matrix.izumbankapp.model.transactions.TransactionResponse;
import org.matrix.izumbankapp.scheduler.DepositScheduler;
import org.matrix.izumbankapp.service.*;
import org.springframework.security.crypto.password.PasswordEncoder;
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

    private final PasswordEncoder passwordEncoder;
    private final TransactionService transactionService;
    private final ExchangeService exchangeService;
    private final DepositService depositService;
    private final AccountService accountService;
    private final UserService userService;
    private final DepositScheduler depositScheduler;

    @Override
    public ResponseDto activateDepositScheduler(){
        log.info("Starting deposit scheduler today: {}", LocalDate.now());
        depositScheduler.calculateDepositInterest();
        return ResponseDto.builder().responseMessage("Successfully activate deposit scheduler").build();
    }


    @Override
    @Transactional
    public ResponseDto createDepositAccount(DepositRequest depositRequest) {
        log.info("Creating deposit account for user: {}", depositRequest.getUserId());

        AccountCreateDto accountDto = AccountCreateDto.builder()
                .accountType(AccountType.DEPOSIT)
                .accountExpireDate(depositRequest.getDepositExpireDate())
                .availableBalance(calculateInterest(depositRequest.getAmount(), depositRequest.getInterest(),
                        depositRequest.getDepositExpireDate()))
                //TODO yml
                .branchCode("333")
                .currencyType(depositRequest.getCurrencyType())
                .currentBalance(BigDecimal.ZERO)
                .pin(passwordEncoder.encode(depositRequest.getPin()))
                .userId(depositRequest.getUserId())
                .build();

        userService.createCif(depositRequest.getUserId());

        AccountResponse accountResponse = accountService.createAccount(accountDto);
        DepositResponse depositResponse = DepositResponse.builder()
                .account(accountService.getAccountById(accountResponse.getId()))
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

        var fromAccount = accountService.getAccountByAccountNumber(transferMoneyRequest.getFromAccountNumber());
        var toAccount = accountService.getAccountByAccountNumber(transferMoneyRequest.getToAccountNumber());
        if (fromAccount.getAccountType() == AccountType.DEPOSIT || toAccount.getAccountType() == AccountType.DEPOSIT) {
            throw new TransferException("Invalid account type: " + AccountType.DEPOSIT);
        }
        if (fromAccount.getStatus() != AccountStatus.ACTIVE || toAccount.getStatus() != AccountStatus.ACTIVE) {
            throw new TransferException("Account status is not active!");
        }
        BigDecimal transferAmount = transferMoneyRequest.getTransactionAccountRequest().getAmount();

        validateAndDebitAccount(fromAccount, transferMoneyRequest.getPin(), transferAmount, toAccount.getCurrencyType());

        transferMoneyRequest.getTransactionAccountRequest().setAmount(transferAmount);
        var fromTransaction = transactionService.createTransaction(fromAccount.getId(),
                transferMoneyRequest.getTransactionAccountRequest(), TransactionType.TRANSFER);

        BigDecimal updateAmount = toAccount.getCurrentBalance().add(transferAmount);
        accountService.updateBalance(toAccount.getAccountNumber(), updateAmount);
        var toTransaction = transactionService.createTransaction(toAccount.getId(),
                transferMoneyRequest.getTransactionAccountRequest(), TransactionType.TRANSFER);

        return executeTransfer(fromAccount, toAccount, fromTransaction, toTransaction);
    }

    @Override
    @Transactional
    public ResponseDto withdrawal(WithdrawalRequest withdrawalRequest) {
        log.info("Withdrawals from {}. Details: {}",
                withdrawalRequest.getFromAccountNumber(),
                withdrawalRequest.getTransactionAccountRequest());

        AccountResponse fromAccount = accountService.getAccountByAccountNumber(withdrawalRequest.getFromAccountNumber());
        BigDecimal transferAmount = withdrawalRequest.getTransactionAccountRequest().getAmount();

        validateAndDebitAccount(fromAccount, withdrawalRequest.getPin(), transferAmount, withdrawalRequest.getCurrencyType());

        withdrawalRequest.getTransactionAccountRequest().setAmount(transferAmount);
        var fromTransaction = transactionService.createTransaction(fromAccount.getId(),
                withdrawalRequest.getTransactionAccountRequest(), TransactionType.WITHDRAWAL);

        updateTransactionsStatus(List.of(fromTransaction), TransactionStatus.SUCCESSFUL);
        return ResponseDto.builder().responseMessage("Successfully withdrew money").build();
    }

    private void validateAndDebitAccount(AccountResponse fromAccount, String pin, BigDecimal amount, CurrencyType currencyType) {
        accountService.validatePin(fromAccount, pin);
        BigDecimal transferAmount = performCurrencyExchangeIfNeeded(fromAccount, amount, currencyType);
        validateTransaction(fromAccount, transferAmount);
        BigDecimal updateAmount = fromAccount.getCurrentBalance().subtract(transferAmount);
        accountService.updateBalance(fromAccount.getAccountNumber(), updateAmount);
    }

    private BigDecimal performCurrencyExchangeIfNeeded(AccountResponse fromAccount, BigDecimal amount, CurrencyType toCurrency) {
        if (fromAccount.getCurrencyType() != toCurrency) {
            return performCurrencyExchange(fromAccount.getCurrencyType(), amount, toCurrency);
        } else {
            return amount;
        }

    }

    private BigDecimal calculateInterest(BigDecimal amount, BigDecimal interest, LocalDate depositExpireDate) {
        LocalDate currentDate = LocalDate.now();
        Period period = Period.between(currentDate, depositExpireDate);
        int months = period.getMonths() + period.getYears() * 12;

        BigDecimal interestRate = BigDecimal.ZERO.add(interest.divide(BigDecimal.valueOf(100),
                2, RoundingMode.HALF_UP));
        BigDecimal monthlyAmount = amount.multiply(interestRate);
        BigDecimal calculatedInterest = monthlyAmount.multiply(BigDecimal.valueOf(months));

        return amount.add(calculatedInterest);
    }


    private BigDecimal performCurrencyExchange(CurrencyType fromCurrency, BigDecimal amount, CurrencyType toCurrency) {
        if (fromCurrency == CurrencyType.AZN) {
            return BigDecimal.valueOf(exchangeService.performExchangeFromAZN(
                    ExchangeRequestDto.builder().amount(amount.doubleValue())
                            .currencyType(toCurrency).build()).getConvertedAmount());
        } else {
            return BigDecimal.valueOf(exchangeService.performExchangeToAZN(
                    ExchangeRequestDto.builder().amount(amount.doubleValue())
                            .currencyType(fromCurrency).build()).getConvertedAmount());
        }
    }

    private void updateTransactionsStatus(List<TransactionResponse> transactions, TransactionStatus transactionStatus) {
        transactions.forEach(transaction -> transactionService.updateTransactionStatus(transaction.getId(), transactionStatus));
    }

    private ResponseDto executeTransfer(AccountResponse fromAccount, AccountResponse toAccount,
                                        TransactionResponse fromTransaction, TransactionResponse toTransaction) {
        try {
            accountService.updateBalance(fromAccount.getAccountNumber(), fromAccount.getCurrentBalance().subtract(fromTransaction.getAmount()));
            accountService.updateBalance(toAccount.getAccountNumber(), toAccount.getCurrentBalance().add(toTransaction.getAmount()));

            updateTransactionsStatus(List.of(fromTransaction, toTransaction), TransactionStatus.SUCCESSFUL);
            return ResponseDto.builder().responseMessage("Successfully transferred money").build();
        } catch (Exception ex) {
            updateTransactionsStatus(List.of(fromTransaction, toTransaction), TransactionStatus.FAILED);
            if (ex instanceof InsufficientFundsException) {
                throw new InsufficientFundsException("Insufficient funds in the source account");
            } else {
                throw new TransferException("Unexpected error during transfer", ex);
            }
        }
    }

    private void validateTransaction(AccountResponse fromAccount, BigDecimal amount) {
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

    private boolean validateTransactionLimit(AccountResponse account, BigDecimal amount) {
        return account.getTransactionLimit() == null ||
                account.getTransactionLimit().compareTo(amount) >= 0;
    }

    private boolean validateSufficientBalance(AccountResponse account, BigDecimal amount) {
        return account.getCurrentBalance().compareTo(amount) >= 0;
    }

    private boolean validateAvailableBalanceAfterTransfer(AccountResponse account, BigDecimal amount) {
        return account.getCurrentBalance().add(amount).compareTo(account.getAvailableBalance()) <= 0;

    }

}
