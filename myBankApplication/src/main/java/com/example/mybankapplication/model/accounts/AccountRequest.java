package com.example.mybankapplication.model.accounts;

import com.example.mybankapplication.enumeration.accounts.AccountStatus;
import com.example.mybankapplication.enumeration.accounts.AccountType;
import com.example.mybankapplication.enumeration.accounts.CurrencyType;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountRequest {
    @NotBlank(message = "Branch code must not be null")
    @Pattern(regexp = "\\d{3}", message = "Branch code must contain 3 digits")
    private String branchCode;
    @Future(message = "Account expire date must be in the future")
    private LocalDate accountExpireDate;
    @NotNull(message = "Currency type must not be null")
    private CurrencyType currencyType;
    @NotNull(message = "Account type must not be null")
    private AccountType accountType;
    @NotNull(message = "Account status must not be null")
    private AccountStatus status;
    @NotNull(message = "Available balance must not be null")
    private BigDecimal availableBalance;
    @NotNull(message = "Current balance must not be null")
    private BigDecimal currentBalance;
    @NotBlank(message = "PIN must not be null")
    @Pattern(regexp = "\\d{4}", message = "PIN should contain 4 digits")
    private String pin;
}
