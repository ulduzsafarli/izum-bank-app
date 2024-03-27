package org.matrix.izumbankapp.model.deposits;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.matrix.izumbankapp.model.accounts.AccountResponse;

import java.math.BigDecimal;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DepositResponse {
    private Long id;
    private BigDecimal amount;
    private BigDecimal interestRate;
    private boolean yearlyInterest;
    private AccountResponse account;
}
