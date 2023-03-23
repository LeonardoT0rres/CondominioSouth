package com.easyvoteapi.service.exceptions;

public class NonRedefinedPasswordException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public NonRedefinedPasswordException(String message) {
        super(message);
    }
}
