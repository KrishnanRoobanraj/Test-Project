package com.trimble.exception;

public class LeaseException extends RuntimeException {
    
    public LeaseException(String message) {
        super(message);
    }
    
    public LeaseException(String message, Throwable cause) {
        super(message, cause);
    }
}
