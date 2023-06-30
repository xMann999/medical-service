package com.seriuszg.medical.exceptions;

import lombok.Getter;
import lombok.Setter;
import org.apache.logging.log4j.util.Supplier;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class PatientNotFoundException extends PatientExceptions {

    public PatientNotFoundException() {
        super("Nie znaleziono pacjenta zarejestrowanego na ten adres e-mail", HttpStatus.NOT_FOUND);
    }
}
