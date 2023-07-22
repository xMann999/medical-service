package com.seriuszg.medical.controller;

import com.seriuszg.medical.model.dto.MessageDto;
import com.seriuszg.medical.model.dto.PatientEditDto;
import com.seriuszg.medical.model.dto.PatientDto;
import com.seriuszg.medical.model.dto.VisitResponse;
import com.seriuszg.medical.service.PatientService;
import com.seriuszg.medical.service.VisitService;
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
    private final VisitService visitService;

    @PostMapping
    public PatientDto createPatient(@RequestBody PatientDto patientDTO) {
        return patientService.savePatient(patientDTO);
    }

    @GetMapping("/{email}")
    public PatientDto getPatient(@PathVariable String email) {
        return patientService.getPatient(email);
    }

    @GetMapping
    public List<PatientDto> showAllPatients() {
        return patientService.getAllPatients();
    }

    @DeleteMapping("/{email}")
    public PatientDto deletePatient(@PathVariable String email) {
        return patientService.deletePatient(email);
    }

    @PatchMapping("/{email}/details")
    public PatientDto editPatientDetails(@PathVariable String email, @RequestBody PatientEditDto patientEditDto) {
        return patientService.updatePatientDetails(email, patientEditDto);
    }

    @PatchMapping("/{email}/password")
    public ResponseEntity<MessageDto> editPatientPassword(@PathVariable String email, @RequestBody String newPassword) {
        MessageDto messageDto = patientService.updatePatientPassword(email, newPassword);
        return ResponseEntity.status(messageDto.getHttpStatus()).body(messageDto);
    }

    @GetMapping("/{email}/visits")
    public List<VisitResponse> getAllAssignedVisitsToSpecificPatient(@PathVariable String email) {
        return visitService.getAllAssignedVisits(email);
    }
}
