package org.matrix.izumbankapp.model.transactions;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionAccountRequest {
    @NotBlank(message="Amount must not be blank")
    private BigDecimal amount;
    @Size(max = 1000, message = "The max size of message is 1000")
    private String comments;
}
