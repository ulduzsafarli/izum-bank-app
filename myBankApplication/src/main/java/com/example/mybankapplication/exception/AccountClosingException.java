package com.example.mybankapplication.exception;

import java.io.Serial;

public class AccountClosingException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;
    public AccountClosingException(String msg) {
        super(msg);
    }

}
