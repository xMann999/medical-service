package com.seriuszg.medical.controller;

import com.seriuszg.medical.model.dto.PatientDto;
import com.seriuszg.medical.model.dto.VisitRequest;
import com.seriuszg.medical.model.dto.VisitResponse;
import com.seriuszg.medical.service.VisitService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/visits")
@RequiredArgsConstructor
public class VisitController {

    private final VisitService visitService;

    @Operation(summary = "Create a new visit", tags = "Visit")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = VisitResponse.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid request data", content = @Content)
    })
    @PostMapping
    public VisitResponse createVisit(@RequestBody VisitRequest visitRequest) {
        return visitService.requestVisit(visitRequest);
    }

    @Operation(summary = "Assign patient to visit", tags = "Visit")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = VisitResponse.class))}),
            @ApiResponse(responseCode = "400", description = "Patient or visit not found", content = @Content)
    })
    @PatchMapping("/{visitId}/assignPatient")
    public VisitResponse assignPatientToVisit(@RequestBody Long patientId, @PathVariable Long visitId) {
        return visitService.assignPatient(visitId, patientId);
    }
}
