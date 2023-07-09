package com.seriuszg.medical.service;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.seriuszg.medical.exceptions.*;
import com.seriuszg.medical.mapper.VisitMapper;
import com.seriuszg.medical.model.dto.VisitRequest;
import com.seriuszg.medical.model.dto.VisitResponse;
import com.seriuszg.medical.model.entity.Patient;
import com.seriuszg.medical.model.entity.Visit;
import com.seriuszg.medical.repositories.PatientRepository;
import com.seriuszg.medical.repositories.VisitRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VisitService {

    private final VisitRepository visitRepository;
    private final VisitMapper visitMapper;
    private final PatientRepository patientRepository;

    private Visit getVisit(Long id) {
        return visitRepository.findById(id).orElseThrow(VisitNotFoundException::new);
    }

    public VisitResponse requestVisit(VisitRequest visitRequest) {
        Visit visit = visitMapper.requestToEntity(visitRequest);
        if (visit.getVisitStartTime().isBefore(LocalDateTime.now())) {
            throw new IncorrectDateException();
        }
        if (!visitRepository.isDateAvailable(visit.getVisitStartTime(), visit.getVisitEndTime())) {
            throw new DateIsUnavailableException();
        }
        if (visit.getVisitStartTime().getSecond() != 0) {
            throw new IncorrectVisitTimeException();
        }
        switch (visit.getVisitStartTime().getMinute()) {
            case 0, 15, 30, 45:
                break;
            default:
                throw new IncorrectVisitTimeException();
        }
        switch (visit.getDuration()) {
            case 15, 30, 45, 60:
                break;
            default:
                throw new IncorrectVisitDutationException();
        }
        return visitMapper.entityToResponse(visitRepository.save(visit));
    }

    public VisitResponse assignPatient(Long id, String email) {
        Visit visit = getVisit(id);
        visit.setPatient(patientRepository.findByEmail(email).orElseThrow(PatientNotFoundException::new));
        visit.setPatientsEmail(patientRepository.findByEmail(email).get().getEmail());
        return visitMapper.entityToResponse(visitRepository.save(visit));
    }

    public List<VisitResponse> getAllAssignedVisits(String email) {
        return visitRepository.findByPatientsEmail(email).stream().map(visit -> visitMapper.entityToResponse(visit)).toList();
    }
}
