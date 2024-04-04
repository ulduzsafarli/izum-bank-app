package org.matrix.izumbankapp.model.accounts;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.matrix.izumbankapp.enumeration.accounts.AccountType;
import org.matrix.izumbankapp.enumeration.accounts.CurrencyType;

import java.math.BigDecimal;
import java.time.LocalDate;

public record AccountCreateDto(
        @NotNull(message = "User ID cannot be null") Long userId,
        @NotBlank(message = "Branch code must not be null")
        @Pattern(regexp = "\\d{3}", message = "Branch code must contain 3 digits") String branchCode,
        @Future(message = "Account expire date must be in the future") LocalDate accountExpireDate,
        @NotNull(message = "Currency type must not be null") CurrencyType currencyType,
        @NotNull(message = "Account type must not be null") AccountType accountType,
        @NotNull(message = "Available balance must not be null") BigDecimal availableBalance,
        @NotNull(message = "Current balance must not be null") BigDecimal currentBalance,
        @DecimalMax(value = "10000", message = "Transaction limit must be at most 10000") BigDecimal transactionLimit,
        @NotBlank(message = "PIN must not be null")
        @Pattern(regexp = "\\d{4}", message = "PIN should contain 4 digits") String pin
) {}
