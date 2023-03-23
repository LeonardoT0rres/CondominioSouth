package com.easyvoteapi.service.exceptions;

public class ScheduleAlreadyExistsException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public ScheduleAlreadyExistsException(String message) {
        super(message);
    }
}
