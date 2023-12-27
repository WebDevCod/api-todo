package com.restapp.rest.myexception;

public class MyException extends RuntimeException {
    public MyException(String message, Throwable cause) {
        super(message, cause);
    }
}
