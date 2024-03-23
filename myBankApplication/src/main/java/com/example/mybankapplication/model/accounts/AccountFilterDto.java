package com.example.mybankapplication.model.accounts;

import com.example.mybankapplication.enumeration.accounts.AccountStatus;
import com.example.mybankapplication.enumeration.accounts.AccountType;
import com.example.mybankapplication.enumeration.accounts.CurrencyType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountFilterDto {
    private String branchCode;
    private LocalDate createdAt;
    private LocalDate accountExpireDate;
    private CurrencyType currencyType;
    private AccountType accountType;
    private AccountStatus status;
    private BigDecimal currentBalance;
    private BigDecimal transactionLimit;
}
