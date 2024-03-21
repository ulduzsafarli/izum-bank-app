package com.example.mybankapplication.model.exchange;

import com.example.mybankapplication.enumeration.accounts.CurrencyType;
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
