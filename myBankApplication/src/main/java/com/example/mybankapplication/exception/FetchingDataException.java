package com.example.mybankapplication.exception;

public class FetchingDataException extends RuntimeException {
    public FetchingDataException(String message, Throwable cause) {
        super(message, cause);
    }
}