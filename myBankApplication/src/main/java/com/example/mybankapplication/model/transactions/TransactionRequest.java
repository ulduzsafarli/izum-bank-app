package com.example.mybankapplication.model.transactions;

import com.example.mybankapplication.enumeration.transaction.TransactionStatus;
import com.example.mybankapplication.enumeration.transaction.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionRequest {
    private BigDecimal amount;
    private TransactionType type;
    private TransactionStatus status;
    private String transactionUUID;
    private String comments;
}
