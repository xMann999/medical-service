package com.seriuszg.medical.exceptions;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class NotAllFieldsFilledException extends PatientExceptions {


    public NotAllFieldsFilledException () {
        super("Wypełnij wszystkie pola", HttpStatus.BAD_REQUEST);
    }
}
