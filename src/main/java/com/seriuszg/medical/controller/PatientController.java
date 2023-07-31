package com.seriuszg.medical.controller;

import com.seriuszg.medical.model.dto.*;
import com.seriuszg.medical.service.PatientService;
import com.seriuszg.medical.service.VisitService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/patients")
@RequiredArgsConstructor
public class PatientController {

    private final PatientService patientService;
    private final VisitService visitService;

    @Operation(summary = "Create a new patient", tags = "Patient")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = PatientDto.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid request data", content = @Content)
    })
    @PostMapping
    public PatientDto createPatient(@RequestBody PatientDto patientDTO) {
        return patientService.savePatient(patientDTO);
    }

    @Operation(summary = "Get information about existing patient", tags = "Patient")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = PatientDto.class))}),
            @ApiResponse(responseCode = "400", description = "Patient not found", content = @Content)
    })
    @GetMapping("/{email}")
    public PatientDto getPatient(@PathVariable String email) {
        return patientService.getPatient(email);
    }

    @Operation(summary = "Get information about all existing patients", tags = "Patient")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, array = @ArraySchema(schema = @Schema (implementation = PatientDto.class)))})
    })
    @GetMapping
    public List<PatientDto> showAllPatients() {
        return patientService.getAllPatients();
    }

    @Operation(summary = "Delete patient", tags = "Patient")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = PatientDto.class))}),
            @ApiResponse(responseCode = "400", description = "Patient not found", content = @Content)
    })
    @DeleteMapping("/{email}")
    public PatientDto deletePatient(@PathVariable String email) {
        return patientService.deletePatient(email);
    }

    @Operation(summary = "Edit patient's details", tags = "Patient")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = PatientDto.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid request data", content = @Content)
    })
    @PatchMapping("/{email}/details")
    public PatientDto editPatientDetails(@PathVariable String email, @RequestBody PatientEditDto patientEditDto) {
        return patientService.updatePatientDetails(email, patientEditDto);
    }

    @Operation(summary = "Edit patient's password", tags = "Patient")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = MessageDto.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid request data", content = @Content)
    })
    @PatchMapping("/{email}/password")
    public ResponseEntity<MessageDto> editPatientPassword(@PathVariable String email, @RequestBody String newPassword) {
        MessageDto messageDto = patientService.updatePatientPassword(email, newPassword);
        return ResponseEntity.status(messageDto.getHttpStatus()).body(messageDto);
    }

    @Operation(summary = "Get all visits assigned to specific patient", tags = "Patient")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, array = @ArraySchema(schema = @Schema (implementation = VisitResponse.class)))}),
            @ApiResponse(responseCode = "400", description = "Patient not found", content = @Content)
    })
    @GetMapping("/{email}/visits")
    public List<VisitResponse> getAllAssignedVisitsToSpecificPatient(@PathVariable String email) {
        return visitService.getAllAssignedVisits(email);
    }
}
