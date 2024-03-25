package org.matrix.izumbankapp.model.accounts;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.matrix.izumbankapp.model.transactions.TransactionAccountRequest;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransferMoneyRequest {

    @NotNull(message = "Account number must not be null")
    @Pattern(regexp = "\\d{7}", message = "Account number must contain 7 digits")
    private String fromAccountNumber;

    @NotNull(message = "Account number must not be null")
    @Pattern(regexp = "\\d{7}", message = "Account number must contain 7 digits")
    private String toAccountNumber;

    @NotNull(message = "PIN must not be null")
    @Pattern(regexp = "\\d{4}", message = "PIN should contain 4 digits")
    private String pin;

    TransactionAccountRequest transactionAccountRequest;
}