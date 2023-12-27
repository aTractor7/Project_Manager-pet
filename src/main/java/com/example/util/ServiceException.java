package com.example.util;

public class ServiceException extends RuntimeException{

    public ServiceException(String message, Throwable cause) {
        super(message);
    }
}
