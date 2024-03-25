package org.matrix.izumbankapp.exception.accounts;

import java.io.Serial;

public class AccountClosingException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;
    public AccountClosingException(String msg) {
        super(msg);
    }

}
