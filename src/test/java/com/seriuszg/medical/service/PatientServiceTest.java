package com.seriuszg.medical.service;

import com.seriuszg.medical.exceptions.EmailAlreadyTakenException;
import com.seriuszg.medical.exceptions.IncorrectEmailException;
import com.seriuszg.medical.exceptions.RequiredFieldsNotFilledException;
import com.seriuszg.medical.exceptions.PatientNotFoundException;
import com.seriuszg.medical.mapper.PatientMapper;
import com.seriuszg.medical.model.dto.PatientEditDto;
import com.seriuszg.medical.model.dto.PatientDto;
import com.seriuszg.medical.model.entity.Patient;
import com.seriuszg.medical.model.entity.Visit;
import com.seriuszg.medical.repositories.PatientRepository;
import com.seriuszg.medical.repositories.VisitRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.*;
import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PatientServiceTest {

    @InjectMocks
    PatientService patientService;
    @Mock
    PatientRepository patientRepository;
    @Mock
    PatientMapper patientMapper;
    @Mock
    VisitRepository visitRepository;

    @Test
    void getPatient_PatientFound_PatientReturned() {
        String email = "sg@gmail.com";
        PatientDto patientDTO = createPatientDTO(email, 1L);
        Patient patient = createPatient(email, 1L);
        when(patientRepository.findByEmail(eq(email))).thenReturn(Optional.of(patient));
        when(patientMapper.toDto(eq(patient))).thenReturn(patientDTO);

        var result = patientService.getPatient(email);

        Assertions.assertEquals(patientDTO, result);
        Assertions.assertEquals("sg@gmail.com", result.getEmail());
    }

    @Test
    void getPatient_PatientNotFound_ExceptionThrown() {
        var exception = Assertions.assertThrows(PatientNotFoundException.class, () -> patientService.getPatient(null));

        Assertions.assertEquals("Nie znaleziono pacjenta zarejestrowanego na ten adres e-mail", exception.getMessage());
        Assertions.assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
    }

    @Test
    void savePatient_PatientCreated_PatientDTOReturned() {
        String email = "dd@gmail.com";
        PatientDto patientDTO = createPatientDTO(email, 1L);
        Patient patient = createPatient(email, 1L);
        when(patientRepository.findByEmail(any())).thenReturn(Optional.empty());
        when(patientMapper.toEntity(eq(patientDTO))).thenReturn(patient);
        when(patientRepository.save(eq(patient))).thenReturn(patient);
        when(patientMapper.toDto(eq(patient))).thenReturn(patientDTO);

        var result = patientService.savePatient(patientDTO);

        Assertions.assertEquals(patientDTO, result);
        Assertions.assertEquals("dd@gmail.com", result.getEmail());
    }

    @Test
    void savePatient_IncorrectData_ExceptionThrown() {
        PatientDto patientDTO = createPatientDTO(null, null);

        var exception = Assertions.assertThrows(IncorrectEmailException.class, () -> patientService.savePatient(patientDTO));

        Assertions.assertEquals("Wpisz poprawny adres e-mail", exception.getMessage());
        Assertions.assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
    }

    @Test
    void savePatient_EmailAlreadyRegistered_ExceptionThrown() {
        String email = "ee@gmail.com";
        Patient patient1 = createPatient(email, 1L);
        PatientDto patientDTO = createPatientDTO(email, 1L);
        when(patientRepository.findByEmail(eq(email))).thenReturn(Optional.of(patient1));

        var exception = Assertions.assertThrows(EmailAlreadyTakenException.class, () -> patientService.savePatient(patientDTO));

        Assertions.assertEquals("Ten adres e-mail jest już zajęty", exception.getMessage());
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, exception.getHttpStatus());
    }

    @Test
    void getAllPatients_PatientsFound_PatientsReturned() {
        Patient patient1 = createPatient("1", 1L);
        Patient patient2 = createPatient("2", 2L);
        List<Patient> patients = List.of(patient1, patient2);
        PatientDto patientDto1 = createPatientDTO("1", 1L);
        PatientDto patientDto2 = createPatientDTO("2", 2L);
        List<PatientDto> patientsDTO = List.of(patientDto1, patientDto2);
        when(patientRepository.findAll()).thenReturn(patients);
        when(patientMapper.toDto(eq(patient1))).thenReturn(patientDto1);
        when(patientMapper.toDto(eq(patient2))).thenReturn(patientDto2);

        var result = patientService.getAllPatients();

        Assertions.assertEquals(patientsDTO, result);
        Assertions.assertEquals("1", result.get(0).getEmail());
        Assertions.assertEquals("2", result.get(1).getEmail());
        Assertions.assertEquals(2, result.size());
    }

    @Test
    void deletePatient_PatientFound_PatientDeleted() {
        Patient patient1 = createPatient("sdsd", 1L);
        PatientDto patient1DTO = createPatientDTO("sdsd", 1L);
        when(patientMapper.toDto(eq(patient1))).thenReturn(patient1DTO);
        when(patientRepository.findByEmail(eq("sdsd"))).thenReturn(Optional.of(patient1));
        when(visitRepository.findByPatientEmail(eq("sdsd"))).thenReturn(List.of(new Visit()));
        when(visitRepository.saveAll(any())).thenReturn(List.of(new Visit()));

        var result = patientService.deletePatient("sdsd");

        Mockito.verify(patientRepository).delete(patient1);
        Assertions.assertEquals(patient1DTO, result);
    }

    @Test
    void deletePatient_PatientNotFound_ExceptionThrown() {
        var exception = Assertions.assertThrows(PatientNotFoundException.class, () -> patientService.deletePatient(null));

        Assertions.assertEquals("Nie znaleziono pacjenta zarejestrowanego na ten adres e-mail", exception.getMessage());
        Assertions.assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
    }

    @Test
    void updatePatientDetails_PatientFound_PatientDetailsChanged() {
        Patient patient = createPatient("ewee", 1L);
        PatientEditDto patientEditDto = new PatientEditDto(
                "changedf",
                "changedl",
                "997",
                "sfdd");
        PatientDto patientDto = new PatientDto(1L,
                "sfdd",
                "dsfdf",
                "dfdfdf",
                "changedf",
                "changedl",
                "997",
                LocalDate.of(2019, 3, 3));
        when(patientRepository.findByEmail(eq("ewee"))).thenReturn(Optional.of(patient));
        when(patientMapper.toDto(eq(patient))).thenReturn(patientDto);

        var result = patientService.updatePatientDetails("ewee", patientEditDto);

        Assertions.assertEquals(patientDto, result);
        Assertions.assertEquals("changedf", result.getFirstName());
        Assertions.assertEquals("changedl", result.getLastName());
        Assertions.assertNotEquals("ewee", result.getEmail());
    }

    @Test
    void updatePatientDetails_PatientNotFound_ExceptionThrown() {
        PatientEditDto patientEditDto = new PatientEditDto("dd", "dd", "345", "dd");

        var exception = Assertions.assertThrows(PatientNotFoundException.class, () -> patientService.updatePatientDetails(any(), patientEditDto));

        Assertions.assertEquals("Nie znaleziono pacjenta zarejestrowanego na ten adres e-mail", exception.getMessage());
        Assertions.assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
    }

    @Test
    void updatePatientDetails_InvalidData_ExceptionThrown() {
        String email = "ddd@gmail.com";
        Patient patient = createPatient(email, 1L);
        PatientEditDto patientEditDto = new PatientEditDto(null, "ddd", "123", "dd");
        when(patientRepository.findByEmail(eq(email))).thenReturn(Optional.of(patient));

        var exception = Assertions.assertThrows(RequiredFieldsNotFilledException.class, () -> patientService.updatePatientDetails(email, patientEditDto));

        Assertions.assertEquals("Wypełnij wszystkie wymagane pola", exception.getMessage());
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, exception.getHttpStatus());
    }

    @Test
    void updatePatientPassword_PatientFound_PatientsPasswordUpdated() {
        String email = "eee@gmail.com";
        Patient patient = createPatient(email, 1L);
        when(patientRepository.findByEmail(eq(email))).thenReturn(Optional.of(patient));

        var result = patientService.updatePatientPassword(email, "changed");

        Assertions.assertEquals("changed", patient.getPassword());
    }

    @Test
    void updatePatientPassword_PatientNotFound_ExceptionThrown() {
        var exception = Assertions.assertThrows(PatientNotFoundException.class, () -> patientService.updatePatientPassword(any(), "dd123"));

        Assertions.assertEquals("Nie znaleziono pacjenta zarejestrowanego na ten adres e-mail", exception.getMessage());
        Assertions.assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
    }

    Patient createPatient(String email, Long id) {
        return new Patient(id,
                email,
                "dsfdf",
                "dfdfdf",
                "dffdf",
                "dfdfd",
                "3452352345",
                LocalDate.of(2019, 3, 3),
                null);
    }

    PatientDto createPatientDTO(String email, Long id) {
        return new PatientDto(
                id,
                email,
                "dsfdf",
                "dfdfdf",
                "dffdf",
                "dfdfd",
                "3452352345",
                LocalDate.of(2019, 3, 3));
    }
}
