package com.example.mybankapplication.exception;

public class TransactionLimitException extends RuntimeException {

    public TransactionLimitException(String message) {
        super(message);
    }
}
