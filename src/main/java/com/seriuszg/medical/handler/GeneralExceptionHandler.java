package com.seriuszg.medical.handler;

import com.seriuszg.medical.exceptions.GeneralExceptions;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GeneralExceptionHandler {

    @ExceptionHandler(GeneralExceptions.class)
    public ResponseEntity<String> generalExeptions(GeneralExceptions e) {
        return ResponseEntity.status(e.getHttpStatus()).body(e.getMessage());
    }
}
