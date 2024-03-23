package com.example.mybankapplication.model.transactions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionAccountRequest {
    private BigDecimal amount;
    private String comments;
}
