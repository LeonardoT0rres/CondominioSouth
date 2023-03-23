package com.easyvoteapi.service.exceptions;

public class InvalidScheduleStatusException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public InvalidScheduleStatusException(String message) {
        super(message);
    }
}
