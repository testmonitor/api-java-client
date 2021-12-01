package com.testmonitor.exceptions;

public class NotFoundException extends Exception{
    public NotFoundException(Integer statusCode, String message) {
        super(statusCode, message);
    }
}
