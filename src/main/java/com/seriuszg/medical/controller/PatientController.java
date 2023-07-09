package com.seriuszg.medical.controller;

import com.seriuszg.medical.model.dto.EditedPatientDto;
import com.seriuszg.medical.model.dto.PatientDto;
import com.seriuszg.medical.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/patients")
@RequiredArgsConstructor
public class PatientController {

    private final PatientService patientService;

    @PostMapping
    public PatientDto createPatient(@RequestBody PatientDto patientDTO) {
        return patientService.savePatient(patientDTO);

    }

    @GetMapping("/{email}")
    public ResponseEntity<PatientDto> getPatient(@PathVariable String email) {
        return new ResponseEntity<>(patientService.getPatient(email), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<PatientDto>> showAllPatients() {
        return new ResponseEntity<>(patientService.getAllPatients(), HttpStatus.OK);
    }

    @DeleteMapping("/{email}")
    public PatientDto deletePatient(@PathVariable String email) { // zwrocic usuniete dto
        return patientService.deletePatient(email);
    }

    @PatchMapping("/{email}/details")
    public EditedPatientDto editPatientDetails(@PathVariable String email, @RequestBody EditedPatientDto editedPatientDto) {
        return patientService.updatePatientDetails(email, editedPatientDto);
    }

    @PatchMapping("/{email}/password")
    public ResponseEntity<String> editPatientPassword(@PathVariable String email, @RequestBody String newPassword) {
        patientService.updatePatientPassword(email, newPassword);
        return new ResponseEntity<>("Pomyślnie zmieniono hasło", HttpStatus.OK);
    }
}
