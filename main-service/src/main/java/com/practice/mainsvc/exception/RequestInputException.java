package com.practice.mainsvc.exception;

public class RequestInputException extends RuntimeException {
    public RequestInputException(String message) {
        super(message);
    }
}
