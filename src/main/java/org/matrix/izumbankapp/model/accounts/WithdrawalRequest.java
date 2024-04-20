package org.matrix.izumbankapp.model.accounts;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.matrix.izumbankapp.enumeration.accounts.CurrencyType;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WithdrawalRequest {

    @NotNull(message = "Account number must not be null")
    @Pattern(regexp = "\\d{7}", message = "Account number must contain 7 digits")
    private String fromAccountNumber;
    @NotNull(message = "PIN must not be null")
    @Pattern(regexp = "\\d{4}", message = "PIN should contain 4 digits")
    private String pin;
    @NotNull(message = "Currency type must not be null")
    private CurrencyType currencyType;
    @NotBlank(message = "Amount must not be blank")
    private BigDecimal amount;
}
