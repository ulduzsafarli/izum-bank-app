package org.matrix.izumbankapp.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.matrix.izumbankapp.enumeration.NotificationType;
import org.matrix.izumbankapp.enumeration.transaction.TransactionStatus;
import org.matrix.izumbankapp.enumeration.transaction.TransactionType;
import org.matrix.izumbankapp.model.notifications.NotificationRequest;
import org.matrix.izumbankapp.model.accounts.AccountResponse;
import org.matrix.izumbankapp.model.deposits.DepositResponse;
import org.matrix.izumbankapp.model.transactions.TransactionAccountRequest;
import org.matrix.izumbankapp.service.DepositService;
import org.matrix.izumbankapp.service.NotificationService;
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
    private final TransactionService transactionService;
    private final NotificationService notificationService;

    @Scheduled(cron = "${DEPOSIT_SCHEDULER}")
    @Transactional
    public void calculateDepositInterest() {
        log.info("Starting deposit scheduler for today");
        LocalDate currentDate = LocalDate.now();

        var deposits = depositService.getDepositAccountsCreatedOnDate(currentDate.getDayOfMonth());

        for (DepositResponse deposit : deposits) {
            AccountResponse accountResponse = deposit.getAccount();
            BigDecimal depositInterest = calculateInterest(deposit);
            BigDecimal newBalance = accountResponse.getCurrentBalance().add(depositInterest);
            accountResponse.setCurrentBalance(newBalance);
            depositService.saveDeposit(deposit);
            createTransactionAndNotification(deposit, accountResponse.getUserId(), depositInterest);
        }
        log.info("Successful deposit amount transfer operation");
    }

    private BigDecimal calculateInterest(DepositResponse deposit) {
        BigDecimal amount = deposit.getAmount();
        BigDecimal interestRate = deposit.getInterestRate();
        boolean yearlyInterest = deposit.isYearlyInterest();

        BigDecimal value = amount.multiply(interestRate).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);

        return yearlyInterest ? value : value.divide(BigDecimal.valueOf(12), 2, RoundingMode.HALF_UP);
    }

    private void createTransactionAndNotification(DepositResponse deposit, Long userId, BigDecimal depositInterest) {
        AccountResponse account = deposit.getAccount();

        TransactionAccountRequest transactionRequest = new TransactionAccountRequest();
        transactionRequest.setAmount(depositInterest);
        transactionRequest.setComments("Deposit interest");

        var transactionResponse = transactionService.createTransaction(account.getId(),
                transactionRequest, TransactionType.DEPOSIT);

        if (transactionResponse != null) {
            transactionService.updateTransactionStatus(transactionResponse.getId(), TransactionStatus.SUCCESSFUL);
            notificationService.createNotification(createNotification(userId));
        } else {
            log.error("Failed to create transaction for deposit: {}", deposit.getId());
        }
    }

    private NotificationRequest createNotification(Long userId) {
        return NotificationRequest.builder()
                .type(NotificationType.MESSAGE)
                .message("The deposit interest amount has been successfully sent to your deposit account")
                .userId(userId).build();
    }

}

