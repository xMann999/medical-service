package com.seriuszg.medical.service;

import com.seriuszg.medical.mapper.PatientMapper;
import com.seriuszg.medical.model.dto.EditedPatient;
import com.seriuszg.medical.model.dto.PatientDTO;
import com.seriuszg.medical.model.entity.Patient;
import com.seriuszg.medical.repositories.PatientRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

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
    void getPatientByEmail_PatientFound_PatientReturned() {
        String email = "test@gmail.com";
        PatientDTO patientDTO = createPatientDTO(email);
        Patient patient = createPatient(email, 1L);
        when(patientRepository.findByEmail(eq(email))).thenReturn(Optional.of(patient));
        when(patientMapper.toPatientDto(eq(patient))).thenReturn(patientDTO);

        var result = patientService.getPatient(email);

        Assertions.assertEquals(patientDTO, result);
    }

    @Test
    void savePatient_PatientCreated_PatientDTOReturned() {
        String email = "dd@gmail.com";
        PatientDTO patientDTO = createPatientDTO(email);
        Patient patient = createPatient(email, 1L);
        when(patientRepository.findByEmail(any())).thenReturn(Optional.empty());
        when(patientMapper.toPatient(eq(patientDTO))).thenReturn(patient);
        when(patientRepository.save(eq(patient))).thenReturn(patient);
        when(patientMapper.toPatientDto(eq(patient))).thenReturn(patientDTO);

        var result = patientService.savePatient(patientDTO);

        Assertions.assertEquals(patientDTO, result);
    }

    @Test
    void getAllPatients_PatientsFound_PatientsReturned() {
        Patient patient1 = createPatient("1", 1L);
        Patient patient2 = createPatient("2", 2L);
        List<Patient> patients = List.of(patient1, patient2);
        PatientDTO patientDTO1 = createPatientDTO("1");
        PatientDTO patientDTO2 = createPatientDTO("2");
        List<PatientDTO> patientsDTO = List.of(patientDTO1, patientDTO2);
        when(patientRepository.findAll()).thenReturn(patients);
        when(patientMapper.toPatientDto(eq(patient1))).thenReturn(patientDTO1);
        when(patientMapper.toPatientDto(eq(patient2))).thenReturn(patientDTO2);

        var result = patientService.getAllPatients();

        Assertions.assertEquals(patientsDTO, result);
        Assertions.assertEquals("1", result.get(0).getEmail());
        Assertions.assertEquals("2", result.get(1).getEmail());
    }

    @Test
    void deletePatient_PatientFound_PatientDeleted() {
        Patient patient1 = createPatient("sdsd", 1L);
        PatientDTO patient1DTO = createPatientDTO("sdsd");
        when(patientMapper.toPatientDto(eq(patient1))).thenReturn(patient1DTO);
        when(patientRepository.findByEmail(eq("sdsd"))).thenReturn(Optional.of(patient1));

        var result = patientService.deletePatient("sdsd");

        Mockito.verify(patientRepository).delete(patient1);
        Assertions.assertEquals(patient1DTO, result);
    }

    @Test
    void updatePatientDetails_PatientFound_PatientDetailsChanged() {
        Patient patient1 = createPatient("ewee", 1L);
        EditedPatient editedPatient = new EditedPatient(
                "changedf",
                "changedl",
                "997",
                "sfdd");
        when(patientRepository.findByEmail(eq("ewee"))).thenReturn(Optional.of(patient1));

        var result = patientService.updatePatientDetails("ewee", editedPatient);

        Assertions.assertEquals(editedPatient, result);
        Assertions.assertEquals("changedf", editedPatient.getFirstName());
        Assertions.assertEquals("changedl", editedPatient.getLastName());
        Assertions.assertNotEquals("ewee", editedPatient.getEmail());
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

    Patient createPatient(String email, Long id) {
        return new Patient(id,
                email,
                "dsfdf",
                "dfdfdf",
                "dffdf",
                "dfdfd",
                "3452352345",
                LocalDate.of(2019, 3, 3));
    }

    PatientDTO createPatientDTO(String email) {
        return new PatientDTO(
                email,
                "dsfdf",
                "dfdfdf",
                "dffdf",
                "dfdfd",
                "3452352345",
                LocalDate.of(2019, 3, 3));
    }
}
