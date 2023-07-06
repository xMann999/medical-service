package com.seriuszg.medical.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EditedPatient {

    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String email;

    public boolean doesContainsNull(EditedPatient editedPatient) {
        if (editedPatient.getFirstName() == null
        || editedPatient.getLastName() == null
        || editedPatient.getPhoneNumber() == null
        || editedPatient.getEmail() == null) {
            return true;
        }
        return false;
    }
}
