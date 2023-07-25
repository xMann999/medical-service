package com.seriuszg.medical.model.dto;

import lombok.*;

import java.time.LocalDate;

@Data
@RequiredArgsConstructor
@Builder
public class PatientDto {

    private final Long id;
    private final String email;
    private final String password;
    private final String idCardNo;
    private final String firstName;
    private final String lastName;
    private final String phoneNumber;
    private final LocalDate birthday;
}
