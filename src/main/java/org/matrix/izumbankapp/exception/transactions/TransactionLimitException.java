package org.matrix.izumbankapp.exception.transactions;

public class TransactionLimitException extends RuntimeException {

    public TransactionLimitException(String message) {
        super(message);
    }
}
