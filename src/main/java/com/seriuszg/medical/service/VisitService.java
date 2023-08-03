package com.seriuszg.medical.service;

import com.seriuszg.medical.exceptions.*;
import com.seriuszg.medical.mapper.VisitMapper;
import com.seriuszg.medical.model.dto.VisitRequest;
import com.seriuszg.medical.model.dto.VisitResponse;
import com.seriuszg.medical.model.entity.Patient;
import com.seriuszg.medical.model.entity.Visit;
import com.seriuszg.medical.repositories.PatientRepository;
import com.seriuszg.medical.repositories.VisitRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class VisitService {

    private final VisitRepository visitRepository;
    private final VisitMapper visitMapper;
    private final PatientRepository patientRepository;

    @Transactional
    public VisitResponse requestVisit(VisitRequest visitRequest) {
        Visit visit = visitMapper.requestToEntity(visitRequest);
        log.trace("Checking if date is correct");
        if (visit.getVisitStartTime().isBefore(LocalDateTime.now())) {
            throw new IncorrectDateException("Podana data jest datą w przeszłości");
        }
        if (visit.getVisitStartTime().getSecond() != 0) {
            throw new IncorrectDateException("Wizyty można ustawiać tylko co pełny kwadrans godziny");
        }
        switch (visit.getVisitStartTime().getMinute()) {
            case 0, 15, 30, 45:
                break;
            default:
                throw new IncorrectDateException("Wizyty można ustawiać tylko co pełny kwadrans godziny");
        }
        if (visit.getDuration().toMinutes() % 15 != 0 || visit.getDuration().toMinutes() == 0) {
            throw new IncorrectVisitDutationException();
        }
        log.trace("Checking if date is available");
        if (visitRepository.findAllOverlapping(visit.getVisitStartTime(), visit.getVisitEndTime()).size() > 0) {
            throw new IncorrectDateException("Istnieje już zapisana wizyta w tym terminie");
        }
        log.info("Saving the visit");
        return visitMapper.entityToResponse(visitRepository.save(visit));
    }

    @Transactional
    public VisitResponse assignPatient(Long visitId, Long patientId) {
        Visit visit = getVisit(visitId);
        log.debug("Visit has been found");
        Patient patient = patientRepository.findById(patientId).orElseThrow(PatientNotFoundException::new);
        log.trace("Patient has been found");
        visit.setPatient(patient);
        log.trace("Saving changes");
        return visitMapper.entityToResponse(visitRepository.save(visit));
    }

    public List<VisitResponse> getAllAssignedVisits(String email) {
        log.info("Fetching all visits");
        return visitRepository.findByPatientEmail(email).stream().map(visit -> visitMapper.entityToResponse(visit)).toList();
    }

    private Visit getVisit(Long id) {
        log.debug("Searching for the visit");
        return visitRepository.findById(id).orElseThrow(VisitNotFoundException::new);
    }
}
