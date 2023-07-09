package com.seriuszg.medical.repositories;

import com.seriuszg.medical.model.entity.Patient;
import com.seriuszg.medical.model.entity.Visit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.*;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {

    Optional<Patient> findByEmail (String email);
}
