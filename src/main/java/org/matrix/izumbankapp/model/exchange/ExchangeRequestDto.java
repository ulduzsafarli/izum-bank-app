package org.matrix.izumbankapp.model.exchange;

import lombok.Builder;
import org.matrix.izumbankapp.enumeration.accounts.CurrencyType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExchangeRequestDto {
    private Double amount;
    private CurrencyType currencyType;
}
