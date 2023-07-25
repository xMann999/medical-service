package com.seriuszg.medical.model.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class DoctorEditDto {

    private final String email;
    private final String firstName;
    private final String lastName;
    private final Specialisation specialisation;

    public boolean isCorrect() {
        return this.email != null &&
                this.firstName != null &&
                this.lastName != null &&
                this.specialisation != null;
    }
}
