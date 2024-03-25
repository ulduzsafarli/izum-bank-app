package org.matrix.izumbankapp.exception.transactions;

import java.io.Serial;

public class TransactionAmountException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    public TransactionAmountException(String message) {
        super(message);
    }
}
