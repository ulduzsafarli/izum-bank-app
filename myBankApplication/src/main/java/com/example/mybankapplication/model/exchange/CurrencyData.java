package com.example.mybankapplication.model.exchange;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CurrencyData {
    String code;
    String nominal;
    String name;
    Double value;
    public boolean isComplete() {
        return code != null && nominal != null && name != null && value != null;
    }

    @Override
    public String toString() {
        return "Currency Code: " + code + "\n" +
                "Currency Nominal: " + nominal + "\n" +
                "Currency Name: " + name + "\n" +
                "Currency Rate: " + value;
    }
}
