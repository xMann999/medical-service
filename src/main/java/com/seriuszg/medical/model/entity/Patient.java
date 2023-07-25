package com.seriuszg.medical.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.seriuszg.medical.model.dto.PatientEditDto;
import jakarta.persistence.*;
import lombok.*;

import java.util.*;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@Entity
@NoArgsConstructor
@Table(name = "patient")
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    private String email;
    private String password;
    private String idCardNo;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private LocalDate birthday;
    @JsonIgnoreProperties("patient")
    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Visit> visits = new HashSet<>();

    public void updateDetails(PatientEditDto patientEditDto) {
        this.email = patientEditDto.getEmail();
        this.firstName = patientEditDto.getFirstName();
        this.lastName = patientEditDto.getLastName();
        this.phoneNumber = patientEditDto.getPhoneNumber();
    }
}
