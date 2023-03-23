package com.easyvoteapi.service.exceptions;

public class InvalidAssemblyStartException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public InvalidAssemblyStartException(String message) {
        super(message);
    }
}
