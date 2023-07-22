package com.seriuszg.medical.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.seriuszg.medical.model.dto.DoctorEditDto;
import com.seriuszg.medical.model.dto.Specialisation;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@AllArgsConstructor
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    @Enumerated(EnumType.STRING)
    private Specialisation specialisation;
    @JsonIgnoreProperties("doctors")
    @ManyToOne
    @JoinColumn(name = "facility_id")
    private Facility facility;

    public void updateDetails(DoctorEditDto doctorEditDto) {
        this.email = doctorEditDto.getEmail();
        this.firstName = doctorEditDto.getFirstName();
        this.lastName = doctorEditDto.getLastName();
        this.specialisation = doctorEditDto.getSpecialisation();
    }
}
