package com.example.mybankapplication.exception;

import org.springframework.dao.DataAccessException;

public class DatabaseException extends RuntimeException {
    public DatabaseException(String message, DataAccessException cause) {
        super(message);
        this.initCause(cause);
    }

    public DataAccessException getDatabaseCause() {
        return (DataAccessException) getCause();
    }
}

