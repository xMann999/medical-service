package com.seriuszg.medical.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EditedPatientDto {

    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String email;

    public boolean doesContainsNull() {
        if (this.getFirstName() == null
        || this.getLastName() == null
        || this.getPhoneNumber() == null
        || this.getEmail() == null) {
            return true;
        }
        return false;
    }
}
