package com.seriuszg.medical.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public class MessageDto {

    private String message;
    private String label;
    private HttpStatus httpStatus;
}
