package com.seriuszg.medical.model.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class DoctorDto {

    private final Long id;
    private final String email;
    private final String firstName;
    private final String lastName;
    private final Specialisation specialisation;
    private final Long facilityId;
}
