package org.matrix.izumbankapp.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.matrix.izumbankapp.enumeration.transaction.TransactionStatus;
import org.matrix.izumbankapp.enumeration.transaction.TransactionType;
import org.matrix.izumbankapp.model.accounts.AccountResponse;
import org.matrix.izumbankapp.model.deposits.DepositResponse;
import org.matrix.izumbankapp.model.transactions.TransactionAccountRequest;
import org.matrix.izumbankapp.model.transactions.TransactionResponse;
import org.matrix.izumbankapp.service.AccountService;
import org.matrix.izumbankapp.service.DepositService;
import org.matrix.izumbankapp.service.TransactionService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

@Slf4j
@Component
@RequiredArgsConstructor
public class DepositScheduler {

    private final DepositService depositService;
    private final AccountService accountService;
    private final TransactionService transactionService;

    @Scheduled(cron = "0 0 0 * * *")
    @Transactional
    public void calculateDepositInterest() {
        log.info("Starting deposit scheduler for today");
        LocalDate currentDate = LocalDate.now();

        var accountResponses = accountService.getDepositAccountsCreatedOnDate(currentDate.getDayOfMonth());

        for (AccountResponse account : accountResponses) {
            var deposit = depositService.getDepositByAccountId(account.getId());
            BigDecimal depositInterest = calculateInterest(deposit);
            BigDecimal newBalance = account.getCurrentBalance().add(depositInterest);
            account.setCurrentBalance(newBalance);

            createTransactionForDeposit(deposit);

            accountService.saveAccount(account);
        }
        log.info("Successful deposit amount transfer operation");
    }


    private BigDecimal calculateInterest(DepositResponse deposit) {
        BigDecimal amount = deposit.getAmount();
        BigDecimal interestRate = deposit.getInterestRate();
        boolean yearlyInterest = deposit.isYearlyInterest();

        BigDecimal depositInterest;
        if (yearlyInterest) {
            depositInterest = amount.multiply(interestRate);
        } else {
            depositInterest = amount.multiply(interestRate.divide(BigDecimal.valueOf(12), 2, RoundingMode.HALF_UP));
        }
        return depositInterest;
    }

    void createTransactionForDeposit(DepositResponse deposit) {
        AccountResponse account = deposit.getAccount();

        TransactionAccountRequest transactionRequest = new TransactionAccountRequest();
        transactionRequest.setAmount(calculateInterest(deposit));
        transactionRequest.setComments("Deposit interest");

        TransactionResponse transactionResponse = transactionService
                .createTransaction(account.getId(), transactionRequest, TransactionType.DEPOSIT);

        if (transactionResponse != null) {
            transactionService.updateTransactionStatus(transactionResponse.getId(), TransactionStatus.SUCCESSFUL);
        } else {
            log.error("Failed to create transaction for deposit: {}", deposit.getId());
        }
    }

}

