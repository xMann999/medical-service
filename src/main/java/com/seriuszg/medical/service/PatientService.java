package com.seriuszg.medical.service;

import com.seriuszg.medical.exceptions.EmailAlreadyTakenException;
import com.seriuszg.medical.exceptions.IncorrectEmailException;
import com.seriuszg.medical.exceptions.RequiredFieldsNotFilledException;
import com.seriuszg.medical.exceptions.PatientNotFoundException;
import com.seriuszg.medical.mapper.PatientMapper;
import com.seriuszg.medical.model.dto.PatientEditDto;
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

    public PatientEditDto updatePatientDetails(String email, PatientEditDto patientEditDto) {
        Patient patient = getPatientByEmail(email);
        if (patientEditDto.doesContainsNull()) {
            throw new RequiredFieldsNotFilledException();
        }
        if (patientRepository.findByEmail(patientEditDto.getEmail()).isPresent() && !email.equals(patientEditDto.getEmail())) {
            throw new EmailAlreadyTakenException();
        }
        patient.setFirstName(patientEditDto.getFirstName());
        patient.setLastName(patientEditDto.getLastName());
        patient.setPhoneNumber(patientEditDto.getPhoneNumber());
        patient.setEmail(patientEditDto.getEmail());
        patientRepository.save(patient);
        return patientEditDto;
    }

    public boolean updatePatientPassword(String email, String newPassword) {
        if (newPassword == null) {
            throw new RequiredFieldsNotFilledException();
        }
        Patient patient = getPatientByEmail(email);
        patient.setPassword(newPassword);
        patientRepository.save(patient);
        return true;
    }

    private Patient getPatientByEmail(String email) {
        return patientRepository.findByEmail(email).orElseThrow(PatientNotFoundException::new);
    }

}
