package org.matrix.izumbankapp.model.accounts;

import org.matrix.izumbankapp.enumeration.accounts.AccountStatus;
import org.matrix.izumbankapp.enumeration.accounts.AccountType;
import org.matrix.izumbankapp.enumeration.accounts.CurrencyType;
import org.matrix.izumbankapp.model.transactions.TransactionResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountResponse {
    private Long id;
    private String branchCode;
    private String accountNumber;
    private LocalDate accountExpireDate;
    private CurrencyType currencyType;
    private AccountType accountType;
    private AccountStatus status;
    private BigDecimal availableBalance;
    private BigDecimal currentBalance;
    private Long userId;
    private BigDecimal transactionLimit;
    private List<TransactionResponse> transactionResponseList;
}
