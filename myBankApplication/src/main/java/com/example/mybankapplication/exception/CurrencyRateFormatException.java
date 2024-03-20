package com.example.mybankapplication.exception;

public class CurrencyRateFormatException extends RuntimeException {

    public CurrencyRateFormatException(String message, Throwable cause) {
        super(message, cause);
    }
}