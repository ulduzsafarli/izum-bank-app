package org.matrix.izumbankapp.exception.accounts;

import java.io.Serial;

public class AccountStatusException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;
    public AccountStatusException(String msg) {
        super(msg);
    }

}
