package com.seriuszg.medical.service;

import com.seriuszg.medical.exceptions.EmailAlreadyTakenException;
import com.seriuszg.medical.exceptions.IncorrectEmailException;
import com.seriuszg.medical.exceptions.RequiredFieldsNotFilledException;
import com.seriuszg.medical.exceptions.PatientNotFoundException;
import com.seriuszg.medical.mapper.PatientMapper;
import com.seriuszg.medical.model.dto.EditedPatientDto;
import com.seriuszg.medical.model.dto.PatientDto;
import com.seriuszg.medical.model.entity.Patient;
import com.seriuszg.medical.repositories.PatientRepository;
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

    @Test
    void getPatient_PatientFound_PatientReturned() {
        String email = "test@gmail.com";
        PatientDto patientDTO = createPatientDTO(email, 1L);
        Patient patient = createPatient(email, 1L);
        when(patientRepository.findByEmail(eq(email))).thenReturn(Optional.of(patient));
        when(patientMapper.toDto(eq(patient))).thenReturn(patientDTO);

        var result = patientService.getPatient(email);

        Assertions.assertEquals(patientDTO, result);
        Assertions.assertEquals("test@gmail.com", result.getEmail());
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
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, exception.getHttpStatus());
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
        EditedPatientDto editedPatientDto = new EditedPatientDto(
                "changedf",
                "changedl",
                "997",
                "sfdd");
        when(patientRepository.findByEmail(eq("ewee"))).thenReturn(Optional.of(patient));

        var result = patientService.updatePatientDetails("ewee", editedPatientDto);

        Assertions.assertEquals(editedPatientDto, result);
        Assertions.assertEquals("changedf", editedPatientDto.getFirstName());
        Assertions.assertEquals("changedl", editedPatientDto.getLastName());
        Assertions.assertNotEquals("ewee", editedPatientDto.getEmail());
    }

    @Test
    void updatePatientDetails_PatientNotFound_ExceptionThrown() {
        EditedPatientDto editedPatientDto = new EditedPatientDto("dd", "dd", "345", "dd");

        var exception = Assertions.assertThrows(PatientNotFoundException.class, () -> patientService.updatePatientDetails(any(), editedPatientDto));

        Assertions.assertEquals("Nie znaleziono pacjenta zarejestrowanego na ten adres e-mail", exception.getMessage());
        Assertions.assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
    }

    @Test
    void updatePatientDetails_InvalidData_ExceptionThrown() {
        String email = "ddd@gmail.com";
        Patient patient = createPatient(email, 1L);
        EditedPatientDto editedPatientDto = new EditedPatientDto(null, "ddd", "123", "dd");
        when(patientRepository.findByEmail(eq(email))).thenReturn(Optional.of(patient));

        var exception = Assertions.assertThrows(RequiredFieldsNotFilledException.class, () -> patientService.updatePatientDetails(email, editedPatientDto));

        Assertions.assertEquals("Wypełnij wszystkie pola", exception.getMessage());
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, exception.getHttpStatus());
    }

    @Test
    void updatePatientPassword_PatientFound_PatientsPasswordUpdated() {
        String email = "eee@gmail.com";
        Patient patient = createPatient(email, 1L);
        when(patientRepository.findByEmail(eq(email))).thenReturn(Optional.of(patient));

        var result = patientService.updatePatientPassword(email, "changed");

        Assertions.assertTrue(result);
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
