package com.seriuszg.medical.controller;

import com.seriuszg.medical.model.dto.DoctorDto;
import com.seriuszg.medical.model.dto.FacilityDto;
import com.seriuszg.medical.model.dto.FacilityRegistrationDto;
import com.seriuszg.medical.service.FacilityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/facilities")
@RequiredArgsConstructor
public class FacilityController {

    private final FacilityService facilityService;

    @Operation(summary = "Create a new facility", tags = "Facility")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = FacilityDto.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid request data", content = @Content)
    })
    @PostMapping
    public FacilityDto saveFacility(@RequestBody FacilityRegistrationDto facilityRegistrationDto) {
        return facilityService.saveFacility(facilityRegistrationDto);
    }

    @Operation(summary = "Delete facility", tags = "Facility")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = FacilityDto.class))}),
            @ApiResponse(responseCode = "400", description = "Facility not found", content = @Content)
    })
    @DeleteMapping("/{id}")
    public FacilityDto deleteFacility (@PathVariable Long id) {
        return facilityService.deleteFacility(id);
    }

    @Operation(summary = "Get information about all existing facilities", tags = "Facility")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, array = @ArraySchema(schema = @Schema (implementation = FacilityDto.class)))})
    })
    @GetMapping
    public List<FacilityDto> showAllFacilities() {
        return facilityService.getAllFacilities();
    }
}
