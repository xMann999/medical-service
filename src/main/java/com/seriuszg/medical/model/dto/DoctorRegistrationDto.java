package com.seriuszg.medical.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DoctorRegistrationDto {

    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private Specialisation specialisation;

    public boolean isCorrect() {
        return this.email != null &&
                this.password != null &&
                this.firstName != null &&
                this.lastName != null &&
                this.specialisation != null;
    }
}
