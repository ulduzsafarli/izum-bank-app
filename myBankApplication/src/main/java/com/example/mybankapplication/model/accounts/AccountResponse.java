package com.example.mybankapplication.model.accounts;

import com.example.mybankapplication.enumeration.AccountStatus;
import com.example.mybankapplication.enumeration.AccountType;
import com.example.mybankapplication.enumeration.CurrencyType;
import com.example.mybankapplication.model.customers.CustomerResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountResponse {
    private Long id;
    private String branchCode;
    private String accountNumber;
    private LocalDate accountOpenDate;
    private LocalDate accountExpireDate;
    private CurrencyType currencyType;
    private AccountType accountType;
    private AccountStatus status;
    private BigDecimal availableBalance;
    private BigDecimal currentBalance;
    private String pin;
    private Long customerId;
}
