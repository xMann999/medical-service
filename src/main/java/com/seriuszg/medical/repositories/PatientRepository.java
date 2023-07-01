package com.seriuszg.medical.repositories;

import com.seriuszg.medical.model.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface PatientRepository extends JpaRepository<Patient, String> {

    Optional<Patient> findByEmail (String email);

}
