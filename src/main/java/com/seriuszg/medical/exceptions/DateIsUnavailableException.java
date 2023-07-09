package com.seriuszg.medical.exceptions;

import org.springframework.http.HttpStatus;

public class DateIsUnavailableException extends GeneralExceptions {

    public DateIsUnavailableException() {
        super("Istnieje już zapisana wizyta w tym terminie", HttpStatus.BAD_REQUEST);
    }
}
