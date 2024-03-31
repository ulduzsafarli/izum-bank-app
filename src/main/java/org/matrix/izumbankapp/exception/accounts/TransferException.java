package org.matrix.izumbankapp.exception.accounts;

public class TransferException extends RuntimeException {
    public TransferException(String s, Exception ex) {
        super(s, ex);
    }public TransferException(String s) {
        super(s);
    }
}
