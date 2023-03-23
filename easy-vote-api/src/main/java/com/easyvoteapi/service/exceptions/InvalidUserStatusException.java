package com.easyvoteapi.service.exceptions;

public class InvalidUserStatusException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public InvalidUserStatusException(String message) {
        super(message);
    }
}
