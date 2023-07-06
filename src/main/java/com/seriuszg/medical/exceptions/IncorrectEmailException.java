package com.seriuszg.medical.exceptions;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class IncorrectEmailException extends PatientExceptions {

    public IncorrectEmailException() {
        super("Wpisz poprawny adres e-mail", HttpStatus.NOT_FOUND);
    }
}
