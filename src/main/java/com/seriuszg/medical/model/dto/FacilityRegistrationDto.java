package com.seriuszg.medical.model.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Data
public class FacilityRegistrationDto {

    private final String name;
    private final String city;
    private final String postalCode;
    private final String street;
    private final String streetNo;

    public boolean isCorrect() {
        return this.name != null &&
                this.city != null &&
                this.postalCode != null &&
                this.street != null &&
                this.streetNo != null;
    }
}
