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

    public PatientDto getPatient(String email) {
        return patientMapper.toDto(getPatientByEmail(email));
    }

    public PatientDto savePatient(PatientDto patientDTO) {
        if (patientDTO.getEmail() == null) {
            throw new IncorrectEmailException();
        }
        if (patientRepository.findByEmail(patientDTO.getEmail()).isPresent()) {
            throw new EmailAlreadyTakenException();
        }
        Patient patient = patientRepository.save(patientMapper.toEntity(patientDTO));
        return patientMapper.toDto(patient);
    }

    public List<PatientDto> getAllPatients() {
        return patientRepository.findAll().stream()
                .map(patientMapper::toDto)
                .collect(Collectors.toList());
    }

    public PatientDto deletePatient(String email) {
        Patient patient = getPatientByEmail(email);
        patientRepository.delete(patient);
        return patientMapper.toDto(patient);
    }

    public EditedPatientDto updatePatientDetails(String email, EditedPatientDto editedPatientDto) {
        Patient patient = getPatientByEmail(email);
        if (editedPatientDto.doesContainsNull()) {
            throw new RequiredFieldsNotFilledException();
        }
        if (patientRepository.findByEmail(editedPatientDto.getEmail()).isPresent()) {
            throw new EmailAlreadyTakenException();
        }
        patient.setFirstName(editedPatientDto.getFirstName());
        patient.setLastName(editedPatientDto.getLastName());
        patient.setPhoneNumber(editedPatientDto.getPhoneNumber());
        patient.setEmail(editedPatientDto.getEmail());
        patientRepository.save(patient);
        return editedPatientDto;
    }

    public boolean updatePatientPassword(String email, String newPassword) {
        Patient patient = getPatientByEmail(email);
        patient.setPassword(newPassword);
        patientRepository.save(patient);
        return true;
    }

}
