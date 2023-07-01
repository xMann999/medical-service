package com.seriuszg.medical.exceptions;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class PatientIllegalDataException extends PatientExceptions {

    public PatientIllegalDataException() {
        super("Wpisz poprawny adres e-mail", HttpStatus.NOT_FOUND);
    }
}
