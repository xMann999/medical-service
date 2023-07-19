package com.seriuszg.medical.repositories;

import com.seriuszg.medical.model.entity.Visit;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface VisitRepository extends JpaRepository<Visit, Long> {

    Optional<Visit> findById(Long id);

    List<Visit> findByPatientEmail(String patientEmail);

    @Query("select v " +
            "from Visit v " +
            "where v.visitStartTime  < :visitEndTime " +
            "and visitEndTime > :visitStartTime")
    List<Visit> findAllOverlapping(LocalDateTime visitStartTime, LocalDateTime visitEndTime);
}
