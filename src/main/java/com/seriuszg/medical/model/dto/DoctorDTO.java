package com.seriuszg.medical.model.dto;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DoctorDTO {

    @Id
    private Long id;
    private String email;
    private String fistName;
    private String lastName;
    private String specialisation;
}
