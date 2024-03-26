package org.matrix.izumbankapp.exception.transactions;


import java.io.Serial;

public class TransactionValidationException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    public TransactionValidationException(String message) {
        super(message);
    }
}
