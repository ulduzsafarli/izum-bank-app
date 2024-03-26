package org.matrix.izumbankapp.exception.accounts;

public class WithdrawException extends RuntimeException {
    public WithdrawException(String unexpectedErrorDuringTransfer, Exception ex) {
        super(unexpectedErrorDuringTransfer, ex);
    }
}
