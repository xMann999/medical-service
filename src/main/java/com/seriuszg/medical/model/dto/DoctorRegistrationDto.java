package com.seriuszg.medical.model.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class DoctorRegistrationDto {

    private final String email;
    private final String password;
    private final String firstName;
    private final String lastName;
    private final Specialisation specialisation;

    public boolean isCorrect() {
        return this.email != null &&
                this.password != null &&
                this.firstName != null &&
                this.lastName != null &&
                this.specialisation != null;
    }
}
