package com.seriuszg.medical.service;

import com.seriuszg.medical.exceptions.EmailAlreadyTakenException;
import com.seriuszg.medical.exceptions.IncorrectEmailException;
import com.seriuszg.medical.exceptions.RequiredFieldsNotFilledException;
import com.seriuszg.medical.exceptions.PatientNotFoundException;
import com.seriuszg.medical.mapper.PatientMapper;
import com.seriuszg.medical.model.dto.MessageDto;
import com.seriuszg.medical.model.dto.PatientEditDto;
import com.seriuszg.medical.model.dto.PatientDto;
import com.seriuszg.medical.model.entity.Patient;
import com.seriuszg.medical.repositories.PatientRepository;
import com.seriuszg.medical.repositories.VisitRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PatientService {

    private final PatientRepository patientRepository;
    private final PatientMapper patientMapper;
    private final VisitRepository visitRepository;

    public PatientDto getPatient(String email) {
        return patientMapper.toDto(getPatientByEmail(email));
    }

    @Transactional
    public PatientDto savePatient(PatientDto patientDTO) {
        log.trace("Checking if all required fields are filled");
        if (patientDTO.getEmail() == null) {
            throw new IncorrectEmailException();
        }
        if (patientRepository.findByEmail(patientDTO.getEmail()).isPresent()) {
            throw new EmailAlreadyTakenException();
        }
        Patient patient = patientRepository.save(patientMapper.toEntity(patientDTO));
        log.trace("Saving the patient");
        return patientMapper.toDto(patient);
    }

    public List<PatientDto> getAllPatients() {
        log.info("Fetching all patients");
        return patientRepository.findAll().stream()
                .map(patientMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public PatientDto deletePatient(String email) {
        Patient patient = getPatientByEmail(email);
        log.debug("Patient has been found");
        visitRepository.findByPatientEmail(email).stream().forEach(visit -> visit.setPatient(null));
        visitRepository.saveAll(visitRepository.findByPatientEmail(email));
        patientRepository.delete(patient);
        log.info("Patient has been removed");
        return patientMapper.toDto(patient);
    }

    @Transactional
    public PatientDto updatePatientDetails(String email, PatientEditDto patientEditDto) {
        Patient patient = getPatientByEmail(email);
        log.debug("Patient has been found");
        if (patientRepository.findByEmail(patientEditDto.getEmail()).isPresent() && !email.equals(patientEditDto.getEmail())) {
            throw new EmailAlreadyTakenException();
        }
        log.trace("Checking if all required fields are filled");
        if (patientEditDto.doesContainsNull()) {
            throw new RequiredFieldsNotFilledException();
        }
        patient.updateDetails(patientEditDto);
        log.trace("Saving changes");
        return patientMapper.toDto(patientRepository.save(patient));
    }

    @Transactional
    public MessageDto updatePatientPassword(String email, String newPassword) {
        if (newPassword == null) {
            throw new RequiredFieldsNotFilledException();
        }
        Patient patient = getPatientByEmail(email);
        log.debug("Patient has been found");
        patient.setPassword(newPassword);
        log.trace("Saving changes");
        patientRepository.save(patient);
        return new MessageDto("Pomyślnie zmieniono hasło", "CHANGE_PASSWORD", HttpStatus.OK);
    }

    private Patient getPatientByEmail(String email) {
        log.debug("Searching for the patient");
        return patientRepository.findByEmail(email).orElseThrow(PatientNotFoundException::new);
    }

}
