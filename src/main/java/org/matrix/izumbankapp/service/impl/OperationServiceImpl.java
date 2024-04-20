package org.matrix.izumbankapp.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.matrix.izumbankapp.enumeration.accounts.*;
import org.matrix.izumbankapp.enumeration.transaction.*;
import org.matrix.izumbankapp.exception.accounts.TransferException;
import org.matrix.izumbankapp.exception.transactions.*;
import org.matrix.izumbankapp.model.accounts.AccountResponse;
import org.matrix.izumbankapp.model.accounts.TransferMoneyRequest;
import org.matrix.izumbankapp.model.accounts.WithdrawalRequest;
import org.matrix.izumbankapp.model.exchange.ExchangeRequestDto;
import org.matrix.izumbankapp.model.transactions.TransactionAccountRequest;
import org.matrix.izumbankapp.model.transactions.TransactionResponse;
import org.matrix.izumbankapp.scheduler.DepositScheduler;
import org.matrix.izumbankapp.service.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class OperationServiceImpl implements OperationService {

    private final TransactionService transactionService;
    private final ExchangeService exchangeService;
    private final AccountService accountService;
    private final DepositScheduler depositScheduler;

    @Override
    public String getBalance(String accountNumber) {
        log.info("Getting current balance from account {}", accountNumber);
        var account = accountService.getByAccountNumber(accountNumber);
        log.info("Get current balance from account {} successfully", accountNumber);
        return account.getCurrentBalance().toString();
    }

    @Override
    public void activateDepositScheduler() {
        log.info("Starting deposit scheduler today: {}", LocalDate.now());
        depositScheduler.calculateDepositInterest();
    }

    @Override
    @Transactional(rollbackFor = {TransactionAmountException.class, TransactionLimitException.class})
    public void transferMoney(TransferMoneyRequest transferMoneyRequest) {

        log.info("Transferring money from {} to {}. Details: {}",
                transferMoneyRequest.getFromAccountNumber(),
                transferMoneyRequest.getToAccountNumber(),
                transferMoneyRequest.getTransactionAccountRequest());

        var fromAccount = accountService.getByAccountNumber(transferMoneyRequest.getFromAccountNumber());
        var toAccount = accountService.getByAccountNumber(transferMoneyRequest.getToAccountNumber());
        if (fromAccount.getAccountType() == AccountType.DEPOSIT || toAccount.getAccountType() == AccountType.DEPOSIT) {
            throw new TransferException("Invalid account type: " + AccountType.DEPOSIT);
        }
        if (fromAccount.getStatus() != AccountStatus.ACTIVE || toAccount.getStatus() != AccountStatus.ACTIVE) {
            throw new TransferException("Account status is not active!");
        }
        BigDecimal transferAmount = transferMoneyRequest.getTransactionAccountRequest().getAmount();

        validateAndDebitAccount(fromAccount, transferMoneyRequest.getPin(), transferAmount, toAccount.getCurrencyType());

        transferMoneyRequest.getTransactionAccountRequest().setAmount(transferAmount);
        var fromTransaction = transactionService.create(fromAccount.getId(),
                transferMoneyRequest.getTransactionAccountRequest(), TransactionType.TRANSFER);

        BigDecimal updateAmount = toAccount.getCurrentBalance().add(transferAmount);
        accountService.updateBalance(toAccount.getAccountNumber(), updateAmount);
        var toTransaction = transactionService.create(toAccount.getId(),
                transferMoneyRequest.getTransactionAccountRequest(), TransactionType.TRANSFER);

        executeTransfer(fromAccount, toAccount, fromTransaction, toTransaction);
    }

    @Override
    @Transactional
    public void withdrawal(WithdrawalRequest withdrawalRequest) {
        log.info("Withdrawals from {}. Details: {}",
                withdrawalRequest.getFromAccountNumber(),
                withdrawalRequest);

        AccountResponse fromAccount = accountService.getByAccountNumber(withdrawalRequest.getFromAccountNumber());
        BigDecimal transferAmount = withdrawalRequest.getAmount();

        validateAndDebitAccount(fromAccount, withdrawalRequest.getPin(), transferAmount, withdrawalRequest.getCurrencyType());

        withdrawalRequest.setAmount(transferAmount);
        var transaction = new TransactionAccountRequest(transferAmount, "Withdrawal");
        var fromTransaction = transactionService.create(fromAccount.getId(), transaction, TransactionType.WITHDRAWAL);

        updateTransactionsStatus(List.of(fromTransaction), TransactionStatus.SUCCESSFUL);
    }

    private void validateAndDebitAccount(AccountResponse fromAccount, String pin, BigDecimal amount, CurrencyType currencyType) {
        accountService.validatePin(fromAccount, pin);
        BigDecimal transferAmount = performExchangeIfNeeded(fromAccount, amount, currencyType);
        validateTransaction(fromAccount, transferAmount);
        BigDecimal updateAmount = fromAccount.getCurrentBalance().subtract(transferAmount);
        accountService.updateBalance(fromAccount.getAccountNumber(), updateAmount);
    }

    private BigDecimal performExchangeIfNeeded(AccountResponse fromAccount, BigDecimal amount, CurrencyType toCurrency) {
        if (fromAccount.getCurrencyType() != toCurrency) {
            return performExchange(fromAccount.getCurrencyType(), amount, toCurrency);
        } else {
            return amount;
        }

    }

    private BigDecimal performExchange(CurrencyType fromCurrency, BigDecimal amount, CurrencyType toCurrency) {
        if (fromCurrency == CurrencyType.AZN) {
            return BigDecimal.valueOf(exchangeService.fromAZN(
                    ExchangeRequestDto.builder().amount(amount.doubleValue())
                            .currencyType(toCurrency).build()).getConvertedAmount());
        } else {
            return BigDecimal.valueOf(exchangeService.toAZN(
                    ExchangeRequestDto.builder().amount(amount.doubleValue())
                            .currencyType(fromCurrency).build()).getConvertedAmount());
        }
    }

    private void updateTransactionsStatus(List<TransactionResponse> transactions, TransactionStatus transactionStatus) {
        transactions.forEach(transaction -> transactionService.updateStatus(transaction.getId(), transactionStatus));
    }

    private void executeTransfer(AccountResponse fromAccount, AccountResponse toAccount,
                                 TransactionResponse fromTransaction, TransactionResponse toTransaction) {
        try {
            accountService.updateBalance(fromAccount.getAccountNumber(), fromAccount.getCurrentBalance().subtract(fromTransaction.getAmount()));
            accountService.updateBalance(toAccount.getAccountNumber(), toAccount.getCurrentBalance().add(toTransaction.getAmount()));

            updateTransactionsStatus(List.of(fromTransaction, toTransaction), TransactionStatus.SUCCESSFUL);
        } catch (Exception ex) {
            updateTransactionsStatus(List.of(fromTransaction, toTransaction), TransactionStatus.FAILED);
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
