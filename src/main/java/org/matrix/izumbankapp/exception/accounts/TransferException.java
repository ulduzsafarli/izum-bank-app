package org.matrix.izumbankapp.exception.accounts;

public class TransferException extends RuntimeException {
    public TransferException(String unexpectedErrorDuringTransfer, Exception ex) {
        super(unexpectedErrorDuringTransfer, ex);
    }
}
