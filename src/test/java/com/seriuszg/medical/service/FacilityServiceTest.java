package com.seriuszg.medical.service;

import com.seriuszg.medical.mapper.FacilityMapper;
import com.seriuszg.medical.model.dto.FacilityDto;
import com.seriuszg.medical.model.dto.FacilityRegistrationDto;
import com.seriuszg.medical.model.entity.Facility;
import com.seriuszg.medical.repositories.FacilityRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FacilityServiceTest {

    @InjectMocks
    FacilityService facilityService;
    @Mock
    FacilityRepository facilityRepository;
    @Mock
    FacilityMapper facilityMapper;

    @Test
    void saveFacility_DataCorrect_FacilityCreated() {
        FacilityRegistrationDto facilityRegistrationDto = new FacilityRegistrationDto("Szpital1","Lodz","92-200","Kopernika","10");
        Facility facility = createFacility("Szpital1", 1L);
        FacilityDto facilityDto = createFacilityDto("Szpital1", 1L);
        when(facilityRepository.findByName(any())).thenReturn(Optional.empty());
        when(facilityMapper.registrationDtoToEntity(eq(facilityRegistrationDto))).thenReturn(facility);
        when(facilityMapper.toDto(eq(facility))).thenReturn(facilityDto);
        when(facilityRepository.save(eq(facility))).thenReturn(facility);

        var result = facilityService.saveFacility(facilityRegistrationDto);

        Assertions.assertEquals("Szpital1", result.getName());
        Assertions.assertEquals(1L, result.getId());
        Assertions.assertEquals("92-200", result.getPostalCode());
    }

    @Test
    void deleteFacility_FacilityFound_FacilityDeleted() {
        Facility facility = createFacility("Szpital1", 1L);
        FacilityDto facilityDto = createFacilityDto("Szpital1", 1L);
        when(facilityRepository.findById(eq(1L))).thenReturn(Optional.of(facility));
        when(facilityMapper.toDto(eq(facility))).thenReturn(facilityDto);

        var result = facilityService.deleteFacility(1L);

        Mockito.verify(facilityRepository).delete(facility);
        Assertions.assertEquals(1L, result.getId());
        Assertions.assertEquals("Szpital1", result.getName());
    }

    @Test
    void getAllFacilities_FacilitiesFound_FacilitiesReturned () {
        Facility facility1 = createFacility("11", 1L);
        Facility facility2 = createFacility("22", 2L);
        FacilityDto facilityDto1 = createFacilityDto("11", 1L);
        FacilityDto facilityDto2 = createFacilityDto("22", 2L);
        List<Facility> facilities = List.of(facility1, facility2);
        List<FacilityDto> facilitiesDto = List.of(facilityDto1, facilityDto2);
        when(facilityRepository.findAll()).thenReturn(facilities);
        when(facilityMapper.toDto(eq(facility1))).thenReturn(facilityDto1);
        when(facilityMapper.toDto(eq(facility2))).thenReturn(facilityDto2);

        var result = facilityService.getAllFacilities();

        Assertions.assertEquals(facilitiesDto, result);
        Assertions.assertEquals("11", result.get(0).getName());
        Assertions.assertEquals(2L, result.get(1).getId());
        Assertions.assertEquals(2, result.size());
    }

    private FacilityDto createFacilityDto(String name, Long id) {
        return new FacilityDto(id, name, "Lodz","92-200","Kopernika","10");
    }

    private Facility createFacility(String name, Long id) {
        return Facility.builder()
                .id(id)
                .name(name)
                .city("Lodz")
                .postalCode("92-200")
                .streetNo("10")
                .build();
    }


}
