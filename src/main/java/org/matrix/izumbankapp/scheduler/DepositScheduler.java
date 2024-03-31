package org.matrix.izumbankapp.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.matrix.izumbankapp.enumeration.accounts.AccountStatus;
import org.matrix.izumbankapp.enumeration.accounts.AccountType;
import org.matrix.izumbankapp.enumeration.transaction.TransactionStatus;
import org.matrix.izumbankapp.enumeration.transaction.TransactionType;
import org.matrix.izumbankapp.model.accounts.AccountResponse;
import org.matrix.izumbankapp.model.auth.ResponseDto;
import org.matrix.izumbankapp.model.deposits.DepositResponse;
import org.matrix.izumbankapp.model.transactions.TransactionAccountRequest;
import org.matrix.izumbankapp.model.transactions.TransactionResponse;
import org.matrix.izumbankapp.service.AccountService;
import org.matrix.izumbankapp.service.DepositService;
import org.matrix.izumbankapp.service.TransactionService;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class DepositScheduler {
    private final DepositService depositService;
    private final AccountService accountService;
    private final TransactionService transactionService;

    //    @Scheduled(cron = "0 0 0 * * *") // Каждый день в полночь
    public ResponseDto calculateDepositInterest() {
        LocalDate currentDate = LocalDate.now();

//        List<AccountResponse> accountResponses = accountService.getDepositsCreatedOnDate(currentDate.getDayOfMonth());
        List<AccountResponse> accountResponses = accountService.getDepositAccountsCreatedOnDate(15);
//        List<DepositResponse> deposits = depositService.getDepositsCreatedOnDate(currentDate.getDayOfMonth());

        log.info("deposits {}", accountResponses);
        for (AccountResponse account : accountResponses) {
            var deposit = depositService.getDepositByAccountId(account.getId());
            BigDecimal depositInterest = calculateInterest(deposit);
            BigDecimal newBalance = account.getCurrentBalance().add(depositInterest);
            account.setCurrentBalance(newBalance);

            createTransactionForDeposit(deposit);

            accountService.saveAccount(account);
        }
        return ResponseDto.builder().responseMessage("Successfully done deposit-interest").build();

    }


    private BigDecimal calculateInterest(DepositResponse deposit) {
        BigDecimal amount = deposit.getAmount();
        BigDecimal interestRate = deposit.getInterestRate();
        boolean yearlyInterest = deposit.isYearlyInterest();

        BigDecimal depositInterest;
        if (yearlyInterest) {
            depositInterest = amount.multiply(interestRate);
        } else {
            depositInterest = amount.multiply(interestRate.divide(BigDecimal.valueOf(12), 2, BigDecimal.ROUND_HALF_UP));
        }
        return depositInterest;
    }

    void createTransactionForDeposit(DepositResponse deposit) {
        AccountResponse account = deposit.getAccount();

        TransactionAccountRequest transactionRequest = new TransactionAccountRequest();
        transactionRequest.setAmount(calculateInterest(deposit));
        transactionRequest.setComments("Deposit interest");

        TransactionResponse transactionResponse = transactionService.createTransaction(account.getId(), transactionRequest, TransactionType.DEPOSIT);

        if (transactionResponse != null) {
            transactionService.updateTransactionStatus(transactionResponse.getId(), TransactionStatus.SUCCESSFUL);
        } else {
            log.error("Failed to create transaction for deposit: {}", deposit.getId());
        }
    }

//
//    public void processDailyInterestAccrual() {
//        LocalDate today = LocalDate.now();
//
//        // JPA Query to retrieve matching accounts
//        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
//        CriteriaQuery<AccountEntity> criteriaQuery = criteriaBuilder.createQuery(AccountEntity.class);
//        Root<AccountEntity> root = criteriaQuery.from(AccountEntity.class);
//
//        criteriaQuery.select(root)
//                .where(
//                        criteriaBuilder.and(
//                                criteriaBuilder.equal(root.get("status"), AccountStatus.ACTIVE),
//                                criteriaBuilder.greaterThan(root.get("accountExpireDate"), today),
//                                criteriaBuilder.equal(criteriaBuilder.function("dayofmonth", root.get("createdDate")), today.getDayOfMonth())
//                        )
//                );
//
//        List<AccountEntity> accounts = entityManager.createQuery(criteriaQuery).getResultList();
//
//        for (AccountEntity account : accounts) {
//            DepositEntity deposit = depositRepository.findById(account.getId()).orElse(null);
//
//            if (deposit != null) {
//                BigDecimal interestRate = deposit.getInterestRate();
//                boolean yearlyInterest = deposit.getYearlyInterest();
//
//                // Calculate time period for interest accrual (adjust based on yearly or daily/monthly interest)
//                int daysSinceLastAccrual = calculateDaysSinceLastAccrual(account, deposit); // Implement this method based on your data model
//
//                // Calculate interest amount based on formula and compounding
//                BigDecimal interestAmount = calculateInterest(account.getCurrentBalance(), interestRate, daysSinceLastAccrual, yearlyInterest);
//
//                account.creditBalance(interestAmount);
//
//                // Optional: Create and persist a TransactionEntity for audit trails
//                TransactionEntity transaction = new TransactionEntity();
//                transaction.setAccount(account);
//                transaction.setTransactionDate(today);
//                transaction.setAmount(interestAmount);
//                transaction.setDescription("Daily Interest Accrual");
//                // ... (set other transaction details)
//                transactionRepository.save(transaction);
//
//                accountRepository.save(account);
//            }
//        }
//    }
//
//    // Implement methods to calculate daysSinceLastAccrual and interest amount based on your data model and interest calculation logic
//    private int calculateDaysSinceLastAccrual(AccountEntity account, DepositEntity deposit) {
//        // ... (logic to determine days since last accrual based on your data model)
//    }
////
////    private BigDecimal calculateInterest(BigDecimal currentBalance, BigDecimal interestRate, int days, boolean yearlyInterest) {
////        // ... (logic to calculate interest amount based on formula and compounding)
////    }
}

