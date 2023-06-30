package com.seriuszg.medical.service;

import com.seriuszg.medical.exceptions.EmailAlreadyTakenException;
import com.seriuszg.medical.exceptions.NotAllFieldsFilledException;
import com.seriuszg.medical.exceptions.PatientIllegalDataException;
import com.seriuszg.medical.exceptions.PatientNotFoundException;
import com.seriuszg.medical.mapper.PatientMapper;
import com.seriuszg.medical.model.dto.PatientDTO;
import com.seriuszg.medical.model.dto.EditedPatient;
import com.seriuszg.medical.model.entity.Patient;
import com.seriuszg.medical.repositories.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PatientService {

    private final PatientRepository patientRepository;
    private final PatientMapper patientMapper;

    private Patient getPatientByEmail(String email) {
        return patientRepository.findByEmail(email).orElseThrow(PatientNotFoundException::new);
    }

    public PatientDTO getPatient(String email) {
        return patientMapper.toPatientDto(getPatientByEmail(email));
    }

    public PatientDTO savePatient(PatientDTO patientDTO) {
        if (patientDTO.getEmail() == null) {
            throw new PatientIllegalDataException();
        }
        if (patientRepository.findByEmail(patientDTO.getEmail()).isPresent()) {
            throw new EmailAlreadyTakenException();
        }
        Patient patient = patientRepository.save(patientMapper.toPatient(patientDTO));
        return patientMapper.toPatientDto(patient);
    }

    public List<PatientDTO> getAllPatients() {
        return patientRepository.findAll().stream().map(patientMapper::toPatientDto).collect(Collectors.toList());
    }

    public PatientDTO deletePatient(String email) {
        Patient patient = getPatientByEmail(email);
        patientRepository.delete(patient);
        return patientMapper.toPatientDto(patient);
    }

    public EditedPatient updatePatientDetails(String email, EditedPatient editedPatient) {
        Patient patient = getPatientByEmail(email);
        if (editedPatient.doesContainsNull(editedPatient)) {
            throw new NotAllFieldsFilledException();
        }
        if (patientRepository.findByEmail(editedPatient.getEmail()).isPresent()) {
            throw new EmailAlreadyTakenException();
        }
        patient.setFirstName(editedPatient.getFirstName());
        patient.setLastName(editedPatient.getLastName());
        patient.setPhoneNumber(editedPatient.getPhoneNumber());
        patient.setEmail(editedPatient.getEmail());
        patientRepository.save(patient);
        return editedPatient;
    }

    public boolean updatePatientPassword(String email, String newPassword) {
        Patient patient = getPatientByEmail(email);
        patient.setPassword(newPassword);
        patientRepository.save(patient);
        return true;
    }

}
