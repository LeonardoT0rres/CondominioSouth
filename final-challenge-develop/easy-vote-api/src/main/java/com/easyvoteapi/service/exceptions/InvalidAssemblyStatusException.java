package com.easyvoteapi.service.exceptions;

public class InvalidAssemblyStatusException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public InvalidAssemblyStatusException(String message) {
        super(message);
    }
}
