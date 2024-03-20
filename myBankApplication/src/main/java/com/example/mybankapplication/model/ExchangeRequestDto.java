package com.example.mybankapplication.model;

import com.example.mybankapplication.enumeration.accounts.CurrencyType;
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
