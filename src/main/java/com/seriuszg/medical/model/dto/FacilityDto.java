package com.seriuszg.medical.model.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Data
public class FacilityDto {

    private final Long id;
    private final String name;
    private final String city;
    private final String postalCode;
    private final String street;
    private final String streetNo;
}
