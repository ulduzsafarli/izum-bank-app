package org.matrix.izumbankapp.exception.supports;

public class EmailSendingException extends RuntimeException {
    public EmailSendingException(String errorWhileSendingEmail, Throwable cause) {
        super(errorWhileSendingEmail, cause);
    }
}
