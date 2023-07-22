package com.seriuszg.medical.controller;

import com.seriuszg.medical.model.dto.FacilityDto;
import com.seriuszg.medical.model.dto.FacilityRegistrationDto;
import com.seriuszg.medical.service.FacilityService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/facilities")
@RequiredArgsConstructor
public class FacilityController {

    private final FacilityService facilityService;

    @PostMapping
    public FacilityDto saveFacility(@RequestBody FacilityRegistrationDto facilityRegistrationDto) {
        return facilityService.saveFacility(facilityRegistrationDto);
    }

    @DeleteMapping("/{id}")
    public FacilityDto deleteFacility (@PathVariable Long id) {
        return facilityService.deleteFacility(id);
    }

    @GetMapping
    public List<FacilityDto> showAllFacilities() {
        return facilityService.getAllFacilities();
    }
}
