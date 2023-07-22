package com.seriuszg.medical.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DoctorDto {

    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private Specialisation specialisation;
    private Long facilityId;
}
