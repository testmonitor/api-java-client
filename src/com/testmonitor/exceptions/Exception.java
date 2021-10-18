package com.testmonitor.exceptions;

import java.io.IOException;

abstract public class Exception extends IOException
{
    protected Integer statusCode;

    protected String message;

    public Exception(Integer statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }

    public Integer getStatusCode() {
        return this.statusCode;
    }
}
