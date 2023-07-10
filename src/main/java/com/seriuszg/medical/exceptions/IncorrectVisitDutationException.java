package com.seriuszg.medical.exceptions;

import org.springframework.http.HttpStatus;

public class IncorrectVisitDutationException extends GeneralException {

    public IncorrectVisitDutationException() {
        super("Długość wizyty może być jedynie wielokrotnością kwadransu", HttpStatus.BAD_REQUEST);
    }
}
