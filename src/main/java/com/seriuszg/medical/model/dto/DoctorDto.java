package com.seriuszg.medical.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DoctorDto {

    private Long id;
    private String email;
    private String fistName;
    private String lastName;
    private String specialisation;
}
