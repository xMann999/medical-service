package com.seriuszg.medical.service;

import com.seriuszg.medical.exceptions.IncorrectDateException;
import com.seriuszg.medical.exceptions.IncorrectVisitDutationException;
import com.seriuszg.medical.exceptions.PatientNotFoundException;
import com.seriuszg.medical.exceptions.VisitNotFoundException;
import com.seriuszg.medical.mapper.VisitMapper;
import com.seriuszg.medical.model.dto.VisitRequest;
import com.seriuszg.medical.model.dto.VisitResponse;
import com.seriuszg.medical.model.entity.Patient;
import com.seriuszg.medical.model.entity.Visit;
import com.seriuszg.medical.repositories.PatientRepository;
import com.seriuszg.medical.repositories.VisitRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class VisitServiceTest {

    @InjectMocks
    VisitService visitService;

    @Mock
    VisitRepository visitRepository;
    @Mock
    VisitMapper visitMapper;
    @Mock
    PatientRepository patientRepository;

    @Test
    void requestVisit_DateIsInPast_ExceptionThrown() {
        VisitRequest visitRequest = new VisitRequest(LocalDateTime.of(2000,3,10,15,15,0), Duration.ofMinutes(15));
        Visit visit = new Visit(1L, null, LocalDateTime.of(2000,3,10,15,15,0), LocalDateTime.of(2000,3,10,15,30,0), Duration.ofMinutes(15));
        when(visitMapper.requestToEntity(eq(visitRequest))).thenReturn(visit);

        var exception = Assertions.assertThrows(IncorrectDateException.class, () -> visitService.requestVisit(visitRequest));

        Assertions.assertEquals("Podana data jest datą w przeszłości", exception.getMessage());
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, exception.getHttpStatus());
    }

    @Test
    void requestVisit_ThereIsOverlappingVisit_ExceptionThrown() {
        VisitRequest visitRequest = new VisitRequest(LocalDateTime.of(2024,3,10,15,15,0), Duration.ofMinutes(15));
        Visit visit = new Visit(1L, null, LocalDateTime.of(2024,3,10,15,15,0), LocalDateTime.of(2024,3,10,15,30,0), Duration.ofMinutes(15));
        when(visitMapper.requestToEntity(eq(visitRequest))).thenReturn(visit);
        when(visitRepository.findAllOverlapping(visit.getVisitStartTime(), visit.getVisitEndTime())).thenReturn(List.of(new Visit()));

        var exception = Assertions.assertThrows(IncorrectDateException.class, () -> visitService.requestVisit(visitRequest));

        Assertions.assertEquals("Istnieje już zapisana wizyta w tym terminie", exception.getMessage());
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, exception.getHttpStatus());
    }

    @Test
    void requestVisit_IncorrectDate_ExceptionThrown() {
        VisitRequest visitRequest = new VisitRequest(LocalDateTime.of(2024,3,10,15,15,5), Duration.ofMinutes(15));
        Visit visit = new Visit(1L, null, LocalDateTime.of(2024,3,10,15,15,5), LocalDateTime.of(2024,3,10,15,30,5), Duration.ofMinutes(15));
        when(visitMapper.requestToEntity(eq(visitRequest))).thenReturn(visit);

        var exception = Assertions.assertThrows(IncorrectDateException.class, () -> visitService.requestVisit(visitRequest));

        Assertions.assertEquals("Wizyty można ustawiać tylko co pełny kwadrans godziny", exception.getMessage());
    }

    @Test
    void requestVisit_IncorrectVisitDuration_ExceptionThrown() {
        VisitRequest visitRequest = new VisitRequest(LocalDateTime.of(2024,3,10,15,15,0), Duration.ofMinutes(20));
        Visit visit = new Visit(1L, null, LocalDateTime.of(2024,3,10,15,15,0), LocalDateTime.of(2024,3,10,15,30,0), Duration.ofMinutes(20));
        when(visitMapper.requestToEntity(eq(visitRequest))).thenReturn(visit);

        var exception = Assertions.assertThrows(IncorrectVisitDutationException.class, () -> visitService.requestVisit(visitRequest));

        Assertions.assertEquals("Długość wizyty może być jedynie wielokrotnością kwadransu", exception.getMessage());
    }

    @Test
    void requestVisit_DateIsCorrect_VisitCreated() {
        VisitRequest visitRequest = new VisitRequest(LocalDateTime.of(2024,3,10,15,15,0), Duration.ofMinutes(15));
        Visit visit = new Visit(1L, null, LocalDateTime.of(2024,3,10,15,15,0), LocalDateTime.of(2024,3,10,15,30,0), Duration.ofMinutes(15));
        VisitResponse visitResponse = new VisitResponse(1L, LocalDateTime.of(2024,3,10,15,15,0), LocalDateTime.of(2024,3,10,15,30,0), null);
        when(visitMapper.requestToEntity(eq(visitRequest))).thenReturn(visit);
        when(visitRepository.findAllOverlapping(visit.getVisitStartTime(), visit.getVisitEndTime())).thenReturn(List.of());
        when(visitMapper.entityToResponse(eq(visit))).thenReturn(visitResponse);
        when(visitRepository.save(eq(visit))).thenReturn(visit);

        var result = visitService.requestVisit(visitRequest);

        Assertions.assertEquals(2024, result.getVisitStartTime().getYear());
        Assertions.assertEquals(15, result.getVisitStartTime().getHour());
        Assertions.assertEquals(null, result.getPatientId());
    }

    @Test
    void assignPatient_VisitNotFound_ExceptionThrown() {
        when(visitRepository.findById(eq(1L))).thenReturn(Optional.empty());

        var exception = Assertions.assertThrows(VisitNotFoundException.class, () -> visitService.assignPatient(1L, 1L));

        Assertions.assertEquals("Nie znaleziono wizyty o podanym ID", exception.getMessage());
    }

    @Test
    void assignPatient_PatientNotFound_ExceptionThrown() {
        Visit visit = new Visit(1L, null, LocalDateTime.of(2024, 3, 10, 15, 15, 0), LocalDateTime.of(2024, 3, 10, 15, 30, 0), Duration.ofMinutes(15));
        when(visitRepository.findById(eq(1L))).thenReturn(Optional.of(visit));
        when(patientRepository.findById(eq(1L))).thenReturn(Optional.empty());

        var exception = Assertions.assertThrows(PatientNotFoundException.class, () -> visitService.assignPatient(1L, 1L));

        Assertions.assertEquals("Nie znaleziono pacjenta zarejestrowanego na ten adres e-mail", exception.getMessage());
        Assertions.assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
    }

    @Test
    void assignPatient_PatientAndVisitFound_PatientAssigned() {
        Visit visit = new Visit(1L, null, LocalDateTime.of(2024, 3, 10, 15, 15, 0), LocalDateTime.of(2024, 3, 10, 15, 30, 0), Duration.ofMinutes(15));
        when(visitRepository.findById(eq(1L))).thenReturn(Optional.of(visit));
        Patient patient = new Patient(1L, "dd", "dd","dd","dd","klaa","232",LocalDate.of(2001,2,2),null);
        when(patientRepository.findById(eq(1L))).thenReturn(Optional.of(patient));
        VisitResponse visitResponse = new VisitResponse(1L, LocalDateTime.of(2024,3,10,15,15,0), LocalDateTime.of(2024,3,10,15,30,0), 1L);
        when(visitMapper.entityToResponse(eq(visit))).thenReturn(visitResponse);
        when(visitRepository.save(eq(visit))).thenReturn(visit);

        var result = visitService.assignPatient(1L, 1L);

        Assertions.assertEquals(1L, result.getPatientId());
        Assertions.assertEquals(15, result.getVisitStartTime().getMinute());
        Assertions.assertEquals(30, result.getVisitEndTime().getMinute());
        Assertions.assertEquals(1L, visit.getPatient().getId());
    }

    @Test
    void getAllAssignedVisits() {
        Visit visit1 = new Visit(1L, null, LocalDateTime.of(2024, 3, 10, 15, 15, 0), LocalDateTime.of(2024, 3, 10, 15, 30, 0), Duration.ofMinutes(15));
        Visit visit2 = new Visit(2L, null, LocalDateTime.of(2024, 3, 11, 15, 15, 0), LocalDateTime.of(2024, 3, 11, 15, 30, 0), Duration.ofMinutes(15));
        when(visitRepository.findByPatientEmail(eq("dd"))).thenReturn(List.of(visit1, visit2));
        VisitResponse visitResponse1 = new VisitResponse(1L, LocalDateTime.of(2024,3,10,15,15,0), LocalDateTime.of(2024,3,10,15,30,0), 1L);
        VisitResponse visitResponse2 = new VisitResponse(2L, LocalDateTime.of(2024,3,11,15,15,0), LocalDateTime.of(2024,3,11,15,30,0), 1L);
        when(visitMapper.entityToResponse(eq(visit1))).thenReturn(visitResponse1);
        when(visitMapper.entityToResponse(eq(visit2))).thenReturn(visitResponse2);

        var result = visitService.getAllAssignedVisits("dd");

        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals(10, result.get(0).getVisitStartTime().getDayOfMonth());
        Assertions.assertEquals(11, result.get(1).getVisitStartTime().getDayOfMonth());
    }

}

