package com.example.mybankapplication.exception;

public class EmailSendingException extends RuntimeException {
    public EmailSendingException(String errorWhileSendingEmail, Throwable cause) {
        super(errorWhileSendingEmail, cause);
    }
}
