package com.seriuszg.medical.exceptions;

import org.springframework.http.HttpStatus;

public class FacilityAlreadyExistsException extends GeneralException{

    public FacilityAlreadyExistsException() {
        super("Placówka została już zarejestrowana", HttpStatus.BAD_REQUEST);
    }
}
