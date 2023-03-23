package com.easyvoteapi.service.exceptions;

public class AssemblyNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public AssemblyNotFoundException(String message) {
        super(message);
    }
}
