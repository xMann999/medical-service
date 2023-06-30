package com.seriuszg.medical.handler;

import com.seriuszg.medical.exceptions.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GeneralExceptionHandler {

    @ExceptionHandler(PatientExceptions.class)
    public ResponseEntity<String> patientsExeptions(PatientExceptions e) {
        return ResponseEntity.status(e.getHttpStatus()).body(e.getMessage());
    }

}
