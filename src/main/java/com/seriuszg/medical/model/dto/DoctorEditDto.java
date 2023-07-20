package com.seriuszg.medical.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DoctorEditDto {

    private String email;
    private String firstName;
    private String lastName;
    private String specialisation;

    public boolean isCorrect() {
        return this.email != null &&
                this.firstName != null &&
                this.lastName != null &&
                this.specialisation != null;
    }
}
