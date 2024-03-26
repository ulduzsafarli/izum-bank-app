package org.matrix.izumbankapp.model.accounts;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.matrix.izumbankapp.enumeration.accounts.AccountStatus;
import org.matrix.izumbankapp.enumeration.accounts.AccountType;
import org.matrix.izumbankapp.enumeration.accounts.CurrencyType;

import java.math.BigDecimal;
import java.time.LocalDate;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountsUserResponse {
    private Long id;
    private String branchCode;
    private String accountNumber;
    private LocalDate accountExpireDate;
    private CurrencyType currencyType;
    private AccountType accountType;
    private AccountStatus status;
    private BigDecimal availableBalance;
    private BigDecimal currentBalance;
    private BigDecimal transactionLimit;
}
