package com.example.mybankapplication.exception;

public class CurrencyFileSavingException extends RuntimeException {
    public CurrencyFileSavingException(String message, Throwable cause) {
        super(message, cause);
    }
}