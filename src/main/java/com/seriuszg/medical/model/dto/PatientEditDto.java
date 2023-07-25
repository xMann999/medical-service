package com.seriuszg.medical.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class PatientEditDto {

    private final String firstName;
    private final String lastName;
    private final String phoneNumber;
    private final String email;

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
