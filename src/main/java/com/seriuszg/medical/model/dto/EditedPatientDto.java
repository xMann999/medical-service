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

    public boolean doesContainsNull(EditedPatientDto editedPatientDto) {
        if (editedPatientDto.getFirstName() == null
        || editedPatientDto.getLastName() == null
        || editedPatientDto.getPhoneNumber() == null
        || editedPatientDto.getEmail() == null) {
            return true;
        }
        return false;
    }
}
