package com.seriuszg.medical.controller;

import com.seriuszg.medical.model.dto.DoctorDto;
import com.seriuszg.medical.model.dto.DoctorRegistrationDto;
import com.seriuszg.medical.model.dto.DoctorEditDto;
import com.seriuszg.medical.model.dto.MessageDto;
import com.seriuszg.medical.service.DoctorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/doctors")
@RequiredArgsConstructor
public class DoctorController {

    private final DoctorService doctorService;

    @Operation(summary = "Create a new doctor", tags = "Doctor")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = DoctorDto.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid request data", content = @Content)
    })
    @PostMapping
    public DoctorDto createDoctor(@RequestBody DoctorRegistrationDto doctorRegistrationDto) {
        return doctorService.saveDoctor(doctorRegistrationDto);
    }

    @Operation(summary = "Get information about existing doctor", tags = "Doctor")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = DoctorDto.class))}),
            @ApiResponse(responseCode = "404", description = "Doctor not found", content = @Content)
    })
    @GetMapping("/{id}")
    public DoctorDto getDoctor(@PathVariable Long id) {
        return doctorService.getDoctor(id);
    }

    @Operation(summary = "Get information about all existing doctors", tags = "Doctor")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, array = @ArraySchema (schema = @Schema (implementation = DoctorDto.class)))})
    })
    @GetMapping
    public List<DoctorDto> showAllDoctors() {
        return doctorService.getAllDoctors();
    }

    @Operation(summary = "Delete doctor", tags = "Doctor")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = DoctorDto.class))}),
            @ApiResponse(responseCode = "404", description = "Doctor not found", content = @Content)
    })
    @DeleteMapping("/{id}")
    public DoctorDto deleteDoctor(@PathVariable Long id) {
        return doctorService.deleteDoctor(id);
    }

    @Operation(summary = "Edit doctor's details", tags = "Doctor")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = DoctorDto.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid request data", content = @Content)
    })
    @PatchMapping("/{id}/details")
    public DoctorDto editDoctorDetails(@PathVariable Long id, @RequestBody DoctorEditDto doctorEditDto) {
        return doctorService.editDoctorDetails(id, doctorEditDto);
    }

    @Operation(summary = "Edit doctor's password", tags = "Doctor")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = MessageDto.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid request data", content = @Content)
    })
    @PatchMapping("/{id}/password")
    public ResponseEntity<MessageDto> editPatientPassword(@PathVariable Long id, @RequestBody String newPassword) {
        MessageDto messageDto = doctorService.editDoctorPassword(id, newPassword);
        return ResponseEntity.status(messageDto.getHttpStatus()).body(messageDto);
    }

    @Operation(summary = "Assigning doctor to facility", tags = "Doctor")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = DoctorDto.class))}),
            @ApiResponse(responseCode = "400", description = "Doctor or facility not found", content = @Content)
    })
    @PatchMapping("/{id}/assign")
    public DoctorDto assignDoctorToFacility(@PathVariable Long id, @RequestBody Long facilityId) {
        return doctorService.assignDoctorToFacility(id, facilityId);
    }
}
