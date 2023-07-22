package com.seriuszg.medical.service;

import com.seriuszg.medical.mapper.DoctorMapper;
import com.seriuszg.medical.model.dto.DoctorDto;
import com.seriuszg.medical.model.dto.DoctorEditDto;
import com.seriuszg.medical.model.dto.DoctorRegistrationDto;
import com.seriuszg.medical.model.dto.Specialisation;
import com.seriuszg.medical.model.entity.Doctor;
import com.seriuszg.medical.model.entity.Facility;
import com.seriuszg.medical.repositories.DoctorRepository;
import com.seriuszg.medical.repositories.FacilityRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DoctorServiceTest {

    @InjectMocks
    DoctorService doctorService;
    @Mock
    DoctorRepository doctorRepository;
    @Mock
    DoctorMapper doctorMapper;
    @Mock
    FacilityRepository facilityRepository;

    @Test
    void saveDoctor_DataCorrect_DoctorSaved() {
        DoctorRegistrationDto doctorRegistrationDto = new DoctorRegistrationDto("11@gmail.com", "eee", "Jan", "Walczyk", Specialisation.oncology);
        DoctorDto doctorDto = createDoctorDto("11@gmail.com", 1L, null);
        Doctor doctor = createDoctor("11@gmail.com", 1L, null);
        when(doctorRepository.findByEmail(any())).thenReturn(Optional.empty());
        when(doctorMapper.registrationDtoToEntity(eq(doctorRegistrationDto))).thenReturn(doctor);
        when(doctorMapper.toDto(eq(doctor))).thenReturn(doctorDto);
        when(doctorRepository.save(eq(doctor))).thenReturn(doctor);

        var result = doctorService.saveDoctor(doctorRegistrationDto);

        Assertions.assertEquals("11@gmail.com", result.getEmail());
        Assertions.assertEquals(1L, result.getId());
    }

    @Test
    void getAllDoctors_DoctorsFound_DoctorsReturned() {
         Doctor doctor1 = createDoctor("11", 1L, null);
         Doctor doctor2 = createDoctor("22", 2L, null);
         List<Doctor> doctors = List.of(doctor1, doctor2);
         DoctorDto doctorDto1 = createDoctorDto("11", 1L, null);
         DoctorDto doctorDto2 = createDoctorDto("22", 2L, null);
         List<DoctorDto> doctorsDto = List.of(doctorDto1, doctorDto2);
         when(doctorRepository.findAll()).thenReturn(doctors);
         when(doctorMapper.toDto(eq(doctor1))).thenReturn(doctorDto1);
         when(doctorMapper.toDto(eq(doctor2))).thenReturn(doctorDto2);

         var result = doctorService.getAllDoctors();

         Assertions.assertEquals(doctorsDto, result);
         Assertions.assertEquals(2, result.size());
         Assertions.assertEquals("22", result.get(1).getEmail());
    }

    @Test
    void getDoctor_DoctorFound_DoctorReturned() {
        Doctor doctor = createDoctor("11@gmail.com", 1L, null);
        DoctorDto doctorDto = createDoctorDto("11@gmail.com", 1L, null);
        when(doctorRepository.findById(eq(1L))).thenReturn(Optional.of(doctor));
        when(doctorMapper.toDto(eq(doctor))).thenReturn(doctorDto);

        var result = doctorService.getDoctor(1L);

        Assertions.assertEquals("11@gmail.com", result.getEmail());
        Assertions.assertEquals(1L, result.getId());
    }

    @Test
    void editDoctorDetails_DataCorrect_DetailsUpdated() {
        DoctorEditDto doctorEditDto = new DoctorEditDto("11@gmail.com", "Andrzej", "Kowalski", Specialisation.dermatology);
        Doctor doctor = createDoctor("11@gmail.com", 1L, null);
        DoctorDto doctorDto = new DoctorDto(1L, "11@gmail.com", "Andrzej", "Kowalski", Specialisation.dermatology, null);
        when(doctorRepository.findByEmail(any())).thenReturn(Optional.of(doctor));
        when(doctorRepository.findById(eq(1L))).thenReturn(Optional.of(doctor));
        when(doctorMapper.toDto(eq(doctor))).thenReturn(doctorDto);
        when(doctorRepository.save(eq(doctor))).thenReturn(doctor);

        var result = doctorService.editDoctorDetails(1L, doctorEditDto);

        Mockito.verify(doctorRepository).save(doctor);
        Assertions.assertEquals("Andrzej", result.getFirstName());
    }

    @Test
    void editDoctorPassword_DoctorFound_PasswordUpdated() {
        Doctor doctor = createDoctor("11", 1L, null);
        when(doctorRepository.findById(eq(1L))).thenReturn(Optional.of(doctor));

        var result = doctorService.editDoctorPassword(1L, "nowehaslo");

        Mockito.verify(doctorRepository).save(doctor);
        Assertions.assertEquals("Pomyślnie zmieniono hasło", result.getMessage());
    }

    @Test
    void assignDoctorToFacility_DoctorAndFacilityFound_DoctorAssigned() {
        Facility facility = createFacility("szpital", 1L);
        Doctor doctor = createDoctor("22@gmail.com", 1L, null);
        DoctorDto doctorDto = createDoctorDto("22@gmail.com", 1L, 1L);
        when(facilityRepository.findById(eq(1L))).thenReturn(Optional.of(facility));
        when(doctorRepository.findById(eq(1L))).thenReturn(Optional.of(doctor));
        when(doctorMapper.toDto(eq(doctor))).thenReturn(doctorDto);
        when(doctorRepository.save(eq(doctor))).thenReturn(doctor);

        var result = doctorService.assignDoctorToFacility(1L, 1L);

        Assertions.assertEquals(1L, result.getFacilityId());
        Assertions.assertEquals("22@gmail.com", result.getEmail());
    }

    private Doctor createDoctor(String email, Long id, Facility facility) {
        return new Doctor(id, email, "eee", "Jan", "Walczyk", Specialisation.oncology, facility);
    }

    private DoctorDto createDoctorDto(String email, Long id, Long facilityId) {
        return new DoctorDto(id, email,"Jan", "Walczyk", Specialisation.oncology, facilityId);
    }

    private Facility createFacility(String name, Long id) {
        return Facility.builder()
                .id(id)
                .name(name)
                .city("Lodz")
                .postalCode("92-200")
                .streetNo("10")
                .build();
    }
}
