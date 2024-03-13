package com.example.mybankapplication.exception;

import java.io.Serial;

public class AccountStatusException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;
    public AccountStatusException(String msg) {
        super(msg);
    }

}
