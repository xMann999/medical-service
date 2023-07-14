package com.seriuszg.medical.exceptions;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class RequiredFieldsNotFilledException extends GeneralException {

    public RequiredFieldsNotFilledException() {
        super("Wypełnij wszystkie wymagane pola", HttpStatus.BAD_REQUEST);
    }
}
