package com.example.mybankapplication.model.accounts;

import com.example.mybankapplication.enumeration.accounts.AccountStatus;
import com.example.mybankapplication.enumeration.accounts.AccountType;
import com.example.mybankapplication.enumeration.accounts.CurrencyType;
import jakarta.validation.constraints.Size;
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
    @Size(min = 0, max = 3, message = "Branch code must contain 3 digits")
    private String branchCode;
    private LocalDate accountOpenDate;
    private LocalDate accountExpireDate;
    private CurrencyType currencyType;
    private AccountType accountType;
    private AccountStatus status;
    private BigDecimal currentBalance;
}
