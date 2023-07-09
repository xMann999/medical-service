package com.seriuszg.medical.repositories;

import com.seriuszg.medical.model.entity.Visit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface VisitRepository extends JpaRepository<Visit, Long> {

    Optional<Visit> findById(Long id);

    List<Visit> findByPatientsEmail(String patientsEmail);

    default boolean isDateAvailable(LocalDateTime start, LocalDateTime end) {
        List<Visit> nonOverlappingDates = findAll().stream().filter(visit -> start.compareTo(visit.getVisitEndTime()) >= 0 || end.compareTo(visit.getVisitStartTime()) <= 0).toList();
        if (nonOverlappingDates.size() == findAll().size()) {
            return true;
        }
        return false;
    }
}
