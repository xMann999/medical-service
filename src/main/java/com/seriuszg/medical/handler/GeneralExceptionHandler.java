package com.seriuszg.medical.handler;

import com.seriuszg.medical.exceptions.GeneralException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GeneralExceptionHandler {

    @ExceptionHandler(GeneralException.class)
    public ResponseEntity<String> generalExeptions(GeneralException e) {
        return ResponseEntity.status(e.getHttpStatus()).body(e.getMessage());
    }
}
