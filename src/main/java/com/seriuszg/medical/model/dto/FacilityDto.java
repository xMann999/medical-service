package com.seriuszg.medical.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class FacilityDto {

    private Long id;
    private String name;
    private String city;
    private String postalCode;
    private String street;
    private String streetNo;
}
