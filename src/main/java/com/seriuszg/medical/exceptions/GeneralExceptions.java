package com.seriuszg.medical.exceptions;


import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public abstract class GeneralExceptions extends RuntimeException {

    private HttpStatus httpStatus;

    public GeneralExceptions(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }
}
