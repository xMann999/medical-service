package com.seriuszg.medical.controller;

import com.seriuszg.medical.model.dto.DoctorDto;
import com.seriuszg.medical.model.dto.DoctorRegistrationDto;
import com.seriuszg.medical.model.dto.DoctorEditDto;
import com.seriuszg.medical.model.dto.MessageDto;
import com.seriuszg.medical.service.DoctorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/doctors")
@RequiredArgsConstructor
public class DoctorController {

    private final DoctorService doctorService;

    @PostMapping
    public DoctorDto createDoctor(@RequestBody DoctorRegistrationDto doctorRegistrationDto) {
        return doctorService.saveDoctor(doctorRegistrationDto);
    }

    @GetMapping("/{id}")
    public DoctorDto getDoctor(@PathVariable Long id) {
        return doctorService.getDoctor(id);
    }

    @GetMapping
    public List<DoctorDto> showAllDoctors() {
        return doctorService.getAllDoctors();
    }

    @DeleteMapping("/{id}")
    public DoctorDto deleteDoctor(@PathVariable Long id) {
        return doctorService.deleteDoctor(id);
    }

    @PatchMapping("/{id}/details")
    public DoctorDto editDoctorDetails(@PathVariable Long id, @RequestBody DoctorEditDto doctorEditDto) {
        return doctorService.editDoctorDetails(id, doctorEditDto);
    }

    @PatchMapping("/{id}/password")
    public ResponseEntity<MessageDto> editPatientPassword(@PathVariable Long id, @RequestBody String newPassword) {
        MessageDto messageDto = doctorService.editDoctorPassword(id, newPassword);
        return ResponseEntity.status(messageDto.getHttpStatus()).body(messageDto);
    }

    @PatchMapping("/{id}/assign")
    public DoctorDto assignDoctorToFacility(@PathVariable Long id, @RequestBody Long facilityId) {
        return doctorService.assignDoctorToFacility(id, facilityId);
    }
}
