package com.seriuszg.medical.controller;

import com.seriuszg.medical.model.dto.DoctorDto;
import com.seriuszg.medical.model.dto.DoctorRegistrationDto;
import com.seriuszg.medical.model.dto.DoctorEditDto;
import com.seriuszg.medical.service.DoctorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/doctors")
@RequiredArgsConstructor
public class DoctorController {

    private final DoctorService doctorService;

    @PostMapping
    public DoctorDto createPatient(@RequestBody DoctorRegistrationDto doctorRegistrationDto) {
        return doctorService.saveDoctor(doctorRegistrationDto);
    }

    @GetMapping("/{email}")
    public DoctorDto getDoctor(@PathVariable String email) {
        return doctorService.getDoctor(email);
    }

    @GetMapping
    public List<DoctorDto> showAllDoctors() {
        return doctorService.getAllDoctors();
    }

    @DeleteMapping("/{email}")
    public DoctorDto deleteDoctor(@PathVariable String email) {
        return doctorService.deleteDoctor(email);
    }

    @PatchMapping("/{email}/details")
    public DoctorEditDto editedDoctorDetails(@PathVariable String email, @RequestBody DoctorEditDto doctorEditDto) {
        return doctorService.editDoctorDetails(email, doctorEditDto);
    }

    @PatchMapping("/{email}/password")
    public ResponseEntity<String> editPatientPassword(@PathVariable String email, @RequestBody String newPassword) {
        doctorService.editDoctorPassword(email, newPassword);
        return new ResponseEntity<>("Pomyślnie zmieniono hasło", HttpStatus.OK);
    }

    @PatchMapping("/{email}/assign")
    public DoctorDto assignDoctorToFacility(@PathVariable String email, @RequestBody Long id) {
        return doctorService.assignDoctorToFacility(email, id);
    }


}
