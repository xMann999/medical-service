package com.seriuszg.medical.exceptions;

import org.springframework.http.HttpStatus;

public class EntityNotFoundException extends GeneralException {

    public EntityNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}
