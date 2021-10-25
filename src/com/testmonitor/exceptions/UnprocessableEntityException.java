package com.testmonitor.exceptions;

public class UnprocessableEntityException extends Exception{
    public UnprocessableEntityException(Integer statusCode, String message) {
        super(statusCode, message);
    }
}
