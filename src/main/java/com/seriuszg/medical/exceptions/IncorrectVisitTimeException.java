package com.seriuszg.medical.exceptions;

import org.springframework.http.HttpStatus;

public class IncorrectVisitTimeException extends GeneralExceptions {

    public IncorrectVisitTimeException() {
        super("Wizyty można ustawiać tylko co pełny kwadrans godziny", HttpStatus.BAD_REQUEST);
    }
}
