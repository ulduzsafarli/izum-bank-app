package org.matrix.izumbankapp.model.transactions;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.matrix.izumbankapp.enumeration.transaction.TransactionStatus;
import org.matrix.izumbankapp.enumeration.transaction.TransactionType;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionFilterDto {
    private BigDecimal fromAmount;
    private BigDecimal toAmount;
    private TransactionType type;
    private TransactionStatus status;
}
