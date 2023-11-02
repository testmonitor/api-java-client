package com.testmonitor.exceptions;

public class NotFoundException extends Exception{
    /**
     * @param statusCode
     * @param message
     */
    public NotFoundException(Integer statusCode, String message) {
        super(statusCode, message);
    }
}
