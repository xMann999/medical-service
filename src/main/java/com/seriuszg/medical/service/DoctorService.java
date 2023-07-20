package com.seriuszg.medical.service;

import com.seriuszg.medical.exceptions.EmailAlreadyTakenException;
import com.seriuszg.medical.exceptions.EntityNotFoundException;
import com.seriuszg.medical.exceptions.RequiredFieldsNotFilledException;
import com.seriuszg.medical.mapper.DoctorMapper;
import com.seriuszg.medical.model.dto.DoctorDto;
import com.seriuszg.medical.model.dto.DoctorRegistrationDto;
import com.seriuszg.medical.model.dto.DoctorEditDto;
import com.seriuszg.medical.model.entity.Doctor;
import com.seriuszg.medical.model.entity.Facility;
import com.seriuszg.medical.repositories.DoctorRepository;
import com.seriuszg.medical.repositories.FacilityRepository;
import lombok.RequiredArgsConstructor;
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

    public DoctorDto getDoctor(String email) {
        return doctorMapper.toDto(getDoctorByEmail(email));
    }

    public List<DoctorDto> getAllDoctors() {
        return doctorRepository.findAll().stream()
                .map(doctorMapper::toDto)
                .collect(Collectors.toList());
    }

    public DoctorDto deleteDoctor(String email) {
        Doctor doctor = getDoctorByEmail(email);
        doctorRepository.delete(doctor);
        return doctorMapper.toDto(doctor);
    }

    public DoctorEditDto editDoctorDetails(String email, DoctorEditDto doctorEditDto) {
        if (!doctorEditDto.isCorrect()) {
            throw new RequiredFieldsNotFilledException();
        }
        Doctor doctor = getDoctorByEmail(email);
        if (doctorRepository.findByEmail(email).isPresent() && !doctorEditDto.getEmail().equals(email)) {
            throw new EmailAlreadyTakenException();
        }
        doctor.setEmail(doctorEditDto.getEmail());
        doctor.setFirstName(doctorEditDto.getFirstName());
        doctor.setLastName(doctorEditDto.getLastName());
        doctor.setSpecialisation(doctorEditDto.getSpecialisation());
        doctorRepository.save(doctor);
        return doctorEditDto;
    }

    public boolean editDoctorPassword(String email, String newPassword) {
        if (newPassword == null) {
            throw new RequiredFieldsNotFilledException();
        }
        Doctor doctor = getDoctorByEmail(email);
        doctor.setPassword(newPassword);
        doctorRepository.save(doctor);
        return true;
    }

    public DoctorDto assignDoctorToFacility (String email, Long id) {
        Facility facility = facilityRepository.findById(id).orElseThrow( () -> new EntityNotFoundException("Nie znaleziono placówki o podanym ID"));
        Doctor doctor = getDoctorByEmail(email);
        doctor.setFacility(facility);
        return doctorMapper.toDto(doctor);
    }

    private Doctor getDoctorByEmail(String email) {
        return doctorRepository.findByEmail(email).orElseThrow( () -> new EntityNotFoundException("Nie znaleziono doktora zarejestrowanego na ten adres e-mail"));
    }
}
