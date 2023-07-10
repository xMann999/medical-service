package com.seriuszg.medical.controller;

import com.seriuszg.medical.model.dto.VisitRequest;
import com.seriuszg.medical.model.dto.VisitResponse;
import com.seriuszg.medical.service.VisitService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/visits")
@RequiredArgsConstructor
public class VisitController {

    private final VisitService visitService;

    @PostMapping
    public VisitResponse createVisit(@RequestBody VisitRequest visitRequest) {
        return visitService.requestVisit(visitRequest);
    }

    @PatchMapping("/{visitId}/assignPatient")
    public VisitResponse assignPatientToVisit(@RequestBody Long patientId, @PathVariable Long visitId) {
        return visitService.assignPatient(visitId, patientId);
    }
}
