package org.matrix.izumbankapp.scheduler;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.matrix.izumbankapp.enumeration.accounts.AccountStatus;
import org.matrix.izumbankapp.enumeration.transaction.TransactionStatus;
import org.matrix.izumbankapp.enumeration.transaction.TransactionType;
import org.matrix.izumbankapp.model.accounts.AccountResponse;
import org.matrix.izumbankapp.model.deposits.DepositResponse;
import org.matrix.izumbankapp.model.transactions.TransactionAccountRequest;
import org.matrix.izumbankapp.model.transactions.TransactionResponse;
import org.matrix.izumbankapp.service.AccountService;
import org.matrix.izumbankapp.service.DepositService;
import org.matrix.izumbankapp.service.TransactionService;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DepositSchedulerTest {

    @Mock
    private DepositService depositService;

    @Mock
    private AccountService accountService;

    @Mock
    private TransactionService transactionService;

    @InjectMocks
    private DepositScheduler depositScheduler;

    @BeforeEach
    void setUp() {
        // Настройка mock объекта depositService для возвращения депозитных счетов
        when(depositService.getDepositsCreatedOnDate(anyInt())).thenReturn(Collections.singletonList(createSampleDeposit()));
    }

    @Test
    void testCalculateDepositInterest() {
        // Предполагаем, что при создании транзакции всегда возвращается непустой объект TransactionResponse
        when(transactionService.createTransaction(anyLong(), any(), any())).thenReturn(new TransactionResponse(/* передайте нужные параметры */));

        // Вызываем метод calculateDepositInterest
        depositScheduler.calculateDepositInterest();

        // Проверяем, что метод createTransactionForDeposit был вызван
        verify(depositScheduler, times(1)).createTransactionForDeposit(any());
    }

    private DepositResponse createSampleDeposit() {
        DepositResponse deposit = new DepositResponse();
        deposit.setId(1L);
        deposit.setAmount(BigDecimal.valueOf(1000));
        deposit.setInterestRate(BigDecimal.valueOf(0.05));
        deposit.setYearlyInterest(false);
        AccountResponse account = new AccountResponse();
        account.setId(1L);
        account.setStatus(AccountStatus.ACTIVE);
        account.setAccountExpireDate(LocalDate.now().plusYears(1)); // Срок действия аккаунта на год вперед
        deposit.setAccount(account);
        return deposit;
    }
}
