package com.example.mybankapplication.exception;

public class JpaSystemException extends RuntimeException{
    public JpaSystemException(String msg){
        super(msg);
    }
}
