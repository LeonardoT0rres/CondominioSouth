package com.easyvoteapi.service.exceptions;

public class AssemblyAlreadyExistsException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public AssemblyAlreadyExistsException(String message) {
        super(message);
    }
}
