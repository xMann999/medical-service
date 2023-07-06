package com.seriuszg.medical.exceptions;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class IncorrectPatientEmailException extends PatientExceptions {

    public IncorrectPatientEmailException() {
        super("Wpisz poprawny adres e-mail", HttpStatus.BAD_REQUEST);
    }
}
