package com.seriuszg.medical.controller;

import com.seriuszg.medical.model.dto.PatientDTO;
import com.seriuszg.medical.model.dto.EditedPatient;
import com.seriuszg.medical.service.PatientService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.*;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/patients")
@RequiredArgsConstructor
public class PatientController {

    private final PatientService patientService;

    @PostMapping
    public PatientDTO createPatient(@RequestBody PatientDTO patientDTO) {
        return patientService.savePatient(patientDTO);

    }

    @GetMapping("/{email}")
    public ResponseEntity<PatientDTO> getPatient(@PathVariable String email) {
        return new ResponseEntity<>(patientService.getPatient(email), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<PatientDTO>> showAllPatients() {
        return new ResponseEntity<>(patientService.getAllPatients(), HttpStatus.OK);
    }

    @DeleteMapping("/{email}")
    public PatientDTO deletePatient(@PathVariable String email) { // zwrocic usuniete dto
        return patientService.deletePatient(email);
    }

    @PatchMapping("/{email}/details")
    public EditedPatient editPatientDetails(@PathVariable String email, @RequestBody EditedPatient editedPatient) {
        return patientService.updatePatientDetails(email, editedPatient);
    }

    @PatchMapping("/{email}/password")
    public ResponseEntity<String> editPatientPassword(@PathVariable String email, @RequestBody String newPassword) {
        patientService.updatePatientPassword(email, newPassword);
        return new ResponseEntity<>("Pomyślnie zmieniono hasło", HttpStatus.OK);
    }

}
