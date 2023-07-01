package com.seriuszg.medical.exceptions;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class EmailAlreadyTakenException extends PatientExceptions {

    public EmailAlreadyTakenException() {
        super("Ten adres e-mail jest już zajęty", HttpStatus.BAD_REQUEST);
    }
}
