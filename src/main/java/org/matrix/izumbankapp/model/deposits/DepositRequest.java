package org.matrix.izumbankapp.model.deposits;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.matrix.izumbankapp.enumeration.accounts.CurrencyType;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DepositRequest {
    @NotNull(message = "User ID cannot be null")
    private Long userId;
    @NotNull(message = "Amount must not be null")
    private BigDecimal amount;
    @Future(message = "Account expire date must be in the future")
    private LocalDate depositExpireDate;
    @NotNull(message = "Currency type must not be null")
    private CurrencyType currencyType;
    @NotBlank(message = "PIN must not be null")
    @Pattern(regexp = "\\d{4}", message = "PIN should contain 4 digits")
    private String pin;
    @NotNull(message = "Interest must not be null")
    private BigDecimal interest;
    @NotNull(message = "Yearly interest must not be null")
    private boolean yearlyInterest;

}
