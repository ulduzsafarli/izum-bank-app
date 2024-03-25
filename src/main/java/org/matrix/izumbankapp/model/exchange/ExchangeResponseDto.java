package org.matrix.izumbankapp.model.exchange;

import org.matrix.izumbankapp.enumeration.accounts.CurrencyType;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExchangeResponseDto {
    private Double amount;
    private CurrencyType fromCurrency;
    private CurrencyType toCurrency;
    private Double convertedAmount;
}
