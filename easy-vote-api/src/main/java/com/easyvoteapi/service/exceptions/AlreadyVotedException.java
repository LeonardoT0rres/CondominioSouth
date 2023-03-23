package com.easyvoteapi.service.exceptions;

public class AlreadyVotedException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public AlreadyVotedException(String message) {
        super(message);
    }
}
