package com.testmonitor.exceptions;

public class ServerException extends Exception {
    public ServerException(Integer statusCode, String message) {
        super(statusCode, message);
    }
}
