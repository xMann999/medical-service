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
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DoctorService {

    private final DoctorRepository doctorRepository;
    private final DoctorMapper doctorMapper;
    private final FacilityRepository facilityRepository;

    public DoctorDto saveDoctor(DoctorRegistrationDto doctorRegistrationDto) {
        if (!doctorRegistrationDto.isCorrect()) {
            throw new RequiredFieldsNotFilledException();
        }
        if (doctorRepository.findByEmail(doctorRegistrationDto.getEmail()).isPresent()) {
            throw new EmailAlreadyTakenException();
        }
        Doctor doctor = doctorMapper.registrationDtoToEntity(doctorRegistrationDto);
        return doctorMapper.toDto(doctorRepository.save(doctor));
    }

    public DoctorDto getDoctor(Long id) {
        return doctorMapper.toDto(getDoctorById(id));
    }

    public List<DoctorDto> getAllDoctors() {
        return doctorRepository.findAll().stream()
                .map(doctorMapper::toDto)
                .collect(Collectors.toList());
    }

    public DoctorDto deleteDoctor(Long id) {
        Doctor doctor = getDoctorById(id);
        doctorRepository.delete(doctor);
        return doctorMapper.toDto(doctor);
    }

    public DoctorEditDto editDoctorDetails(Long id, DoctorEditDto doctorEditDto) {
        if (!doctorEditDto.isCorrect()) {
            throw new RequiredFieldsNotFilledException();
        }
        Doctor doctor = getDoctorById(id);
        if (doctorRepository.findByEmail(doctorEditDto.getEmail()).isPresent() && !doctorEditDto.getEmail().equals(doctor.getEmail())) {
            throw new EmailAlreadyTakenException();
        }
        doctor.setEmail(doctorEditDto.getEmail());
        doctor.setFirstName(doctorEditDto.getFirstName());
        doctor.setLastName(doctorEditDto.getLastName());
        doctor.setSpecialisation(doctorEditDto.getSpecialisation());
        doctorRepository.save(doctor);
        return doctorEditDto;
    }

    public MessageDto editDoctorPassword(Long id, String newPassword) {
        if (newPassword == null) {
            throw new RequiredFieldsNotFilledException();
        }
        Doctor doctor = getDoctorById(id);
        doctor.setPassword(newPassword);
        doctorRepository.save(doctor);
        return new MessageDto("Pomyślnie zmieniono hasło", "CHANGE_PASSWORD", HttpStatus.OK);
    }

    public DoctorDto assignDoctorToFacility (Long id, Long facilityId) {
        Facility facility = facilityRepository.findById(facilityId).orElseThrow( () -> new EntityNotFoundException("Nie znaleziono placówki o podanym ID"));
        Doctor doctor = getDoctorById(id);
        doctor.setFacility(facility);
        return doctorMapper.toDto(doctorRepository.save(doctor));
    }

    private Doctor getDoctorById(Long id) {
        return doctorRepository.findById(id).orElseThrow( () -> new EntityNotFoundException("Nie znaleziono doktora zarejestrowanego na ten adres e-mail"));
    }
    private Doctor getDoctorByEmail(String email) {
        return doctorRepository.findByEmail(email).orElseThrow( () -> new EntityNotFoundException("Nie znaleziono doktora zarejestrowanego na ten adres e-mail"));
    }
}
