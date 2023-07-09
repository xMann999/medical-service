package com.seriuszg.medical.exceptions;

import org.springframework.http.HttpStatus;

public class IncorrectDateException extends GeneralExceptions {

    public IncorrectDateException() {
        super("Podana data jest datą w przeszłości", HttpStatus.BAD_REQUEST);
    }
}
