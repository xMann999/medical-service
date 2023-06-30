package com.seriuszg.medical.exceptions;


import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public abstract class PatientExceptions extends RuntimeException {

    private HttpStatus httpStatus;

    public PatientExceptions(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

}
