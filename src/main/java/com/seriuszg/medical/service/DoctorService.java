package com.seriuszg.medical.service;

import com.seriuszg.medical.exceptions.EmailAlreadyTakenException;
import com.seriuszg.medical.exceptions.EntityNotFoundException;
import com.seriuszg.medical.exceptions.RequiredFieldsNotFilledException;
import com.seriuszg.medical.mapper.DoctorMapper;
import com.seriuszg.medical.model.dto.DoctorDto;
import com.seriuszg.medical.model.dto.DoctorRegistrationDto;
import com.seriuszg.medical.model.dto.DoctorEditDto;
import com.seriuszg.medical.model.dto.MessageDto;
import com.seriuszg.medical.model.entity.Doctor;
import com.seriuszg.medical.model.entity.Facility;
import com.seriuszg.medical.repositories.DoctorRepository;
import com.seriuszg.medical.repositories.FacilityRepository;
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
public class DoctorService {

    private final DoctorRepository doctorRepository;
    private final DoctorMapper doctorMapper;
    private final FacilityRepository facilityRepository;

    @Transactional
    public DoctorDto saveDoctor(DoctorRegistrationDto doctorRegistrationDto) {
        log.trace("Checking if all required fields are filled");
        if (!doctorRegistrationDto.isCorrect()) {
            throw new RequiredFieldsNotFilledException();
        }
        if (doctorRepository.findByEmail(doctorRegistrationDto.getEmail()).isPresent()) {
            throw new EmailAlreadyTakenException();
        }
        Doctor doctor = doctorMapper.registrationDtoToEntity(doctorRegistrationDto);
        log.trace("Saving the doctor");
        return doctorMapper.toDto(doctorRepository.save(doctor));
    }

    public DoctorDto getDoctor(Long id) {
        return doctorMapper.toDto(getDoctorById(id));
    }

    public List<DoctorDto> getAllDoctors() {
        log.info("Fetching all doctors");
        return doctorRepository.findAll().stream()
                .map(doctorMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public DoctorDto deleteDoctor(Long id) {
        Doctor doctor = getDoctorById(id);
        log.debug("Doctor has been found");
        doctorRepository.delete(doctor);
        log.info("Doctor has been removed");
        return doctorMapper.toDto(doctor);
    }

    @Transactional
    public DoctorDto editDoctorDetails(Long id, DoctorEditDto doctorEditDto) {
        log.trace("Checking if all required fields are filled");
        if (!doctorEditDto.isCorrect()) {
            throw new RequiredFieldsNotFilledException();
        }
        Doctor doctor = getDoctorById(id);
        log.debug("Doctor has been found");
        if (doctorRepository.findByEmail(doctorEditDto.getEmail()).isPresent() && !doctorEditDto.getEmail().equals(doctor.getEmail())) {
            throw new EmailAlreadyTakenException();
        }
        doctor.updateDetails(doctorEditDto);
        log.trace("Saving changes");
        return doctorMapper.toDto(doctorRepository.save(doctor));
    }

    @Transactional
    public MessageDto editDoctorPassword(Long id, String newPassword) {
        if (newPassword == null) {
            throw new RequiredFieldsNotFilledException();
        }
        Doctor doctor = getDoctorById(id);
        log.debug("Doctor has been found");
        doctor.setPassword(newPassword);
        log.trace("Saving changes");
        doctorRepository.save(doctor);
        return new MessageDto("Pomyślnie zmieniono hasło", "CHANGE_PASSWORD", HttpStatus.OK);
    }

    @Transactional
    public DoctorDto assignDoctorToFacility (Long id, Long facilityId) {
        Facility facility = facilityRepository.findById(facilityId).orElseThrow( () -> new EntityNotFoundException("Nie znaleziono placówki o podanym ID"));
        log.debug("Facility has been found");
        Doctor doctor = getDoctorById(id);
        log.debug("Doctor has been found");
        doctor.setFacility(facility);
        log.trace("Saving changes");
        return doctorMapper.toDto(doctorRepository.save(doctor));
    }

    private Doctor getDoctorById(Long id) {
        log.debug("Searching for the doctor");
        return doctorRepository.findById(id).orElseThrow( () -> new EntityNotFoundException("Nie znaleziono doktora zarejestrowanego na ten adres e-mail"));
    }
}
