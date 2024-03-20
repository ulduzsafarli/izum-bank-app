package com.example.mybankapplication.exception;


public class CurrencyFilteringException extends RuntimeException {
    public CurrencyFilteringException(String message, Throwable cause) {
        super(message, cause);
    }

    public CurrencyFilteringException(String message) {
        super(message);
    }
}
