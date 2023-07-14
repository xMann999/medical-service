package com.seriuszg.medical.exceptions;

import org.springframework.http.HttpStatus;

public class IncorrectDateException extends GeneralException {

    public IncorrectDateException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
