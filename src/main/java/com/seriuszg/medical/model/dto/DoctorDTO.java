package com.seriuszg.medical.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DoctorDTO {

    private String email;
    private String password;
    private String fistName;
    private String lastName;
    private String specialisation;
}
