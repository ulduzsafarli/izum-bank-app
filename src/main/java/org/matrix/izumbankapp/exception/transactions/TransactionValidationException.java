package org.matrix.izumbankapp.exception.transactions;

import lombok.Getter;

import java.io.Serial;
import java.util.List;

@Getter
public class TransactionValidationException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;
    private final List<String> errors;

    public TransactionValidationException(String message, List<String> errors) {
        super(message);
        this.errors = errors;
    }

}
