package com.seriuszg.medical.exceptions;

import org.springframework.http.HttpStatus;

public class VisitNotFoundException extends GeneralException {

    public VisitNotFoundException() {
        super("Nie znaleziono wizyty o podanym ID", HttpStatus.NOT_FOUND);
    }
}
