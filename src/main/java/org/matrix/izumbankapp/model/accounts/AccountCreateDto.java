package org.matrix.izumbankapp.model.accounts;

import lombok.Builder;
import org.matrix.izumbankapp.enumeration.accounts.AccountStatus;
import org.matrix.izumbankapp.enumeration.accounts.AccountType;
import org.matrix.izumbankapp.enumeration.accounts.CurrencyType;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountCreateDto {
    @NotNull(message = "User ID cannot be null")
    private Long userId;
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
    @DecimalMax(value = "10000", message = "Transaction limit must be at most 10000")
    private BigDecimal transactionLimit;
    @NotBlank(message = "PIN must not be null")
    @Pattern(regexp = "\\d{4}", message = "PIN should contain 4 digits")
    private String pin;

}
