package com.seriuszg.medical.controller;

import com.seriuszg.medical.model.dto.VisitRequest;
import com.seriuszg.medical.model.dto.VisitResponse;
import com.seriuszg.medical.service.VisitService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class VisitController {

    private final VisitService visitService;

    @PostMapping("/visits")
    public VisitResponse requestVisit(@RequestBody VisitRequest visitRequest) {
        return visitService.requestVisit(visitRequest);
    }

    @PatchMapping("/visits/{id}/assignPatient")
    public VisitResponse assignPatientToVisit(@RequestBody String email, @PathVariable Long id) {
        return visitService.assignPatient(id, email);
    }

    @GetMapping("/patients/{email}/visits")
    public ResponseEntity<List<VisitResponse>> getAllAssignedVisitsToSpecificPatient(@PathVariable String email) {
        return ResponseEntity.status(HttpStatus.OK).body(visitService.getAllAssignedVisits(email));
    }
}
