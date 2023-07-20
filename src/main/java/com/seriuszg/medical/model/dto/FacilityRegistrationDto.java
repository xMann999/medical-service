package com.seriuszg.medical.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class FacilityRegistrationDto {

    private String name;
    private String city;
    private String postalCode;
    private String street;
    private String streetNo;

    public boolean isCorrect() {
        return this.name != null &&
                this.city != null &&
                this.postalCode != null &&
                this.street != null &&
                this.streetNo != null;
    }
}
