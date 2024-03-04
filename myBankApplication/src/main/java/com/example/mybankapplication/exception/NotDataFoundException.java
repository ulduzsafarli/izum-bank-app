package com.example.mybankapplication.exception;

import java.io.Serial;

public class NotDataFoundException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;
    public NotDataFoundException(String msg) {
        super(msg);
    }

}
