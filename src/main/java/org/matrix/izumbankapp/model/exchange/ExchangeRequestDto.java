package org.matrix.izumbankapp.model.exchange;

import org.matrix.izumbankapp.enumeration.accounts.CurrencyType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExchangeRequestDto {
    private Double amount;
    private CurrencyType currencyType;
}
