package com.seriuszg.medical.model.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public class MessageDto {

    private final String message;
    private final String label;
    private final HttpStatus httpStatus;
}
