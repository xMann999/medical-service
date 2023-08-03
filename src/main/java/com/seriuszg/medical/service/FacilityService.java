package com.seriuszg.medical.service;

import com.seriuszg.medical.exceptions.EntityNotFoundException;
import com.seriuszg.medical.exceptions.FacilityAlreadyExistsException;
import com.seriuszg.medical.exceptions.RequiredFieldsNotFilledException;
import com.seriuszg.medical.mapper.FacilityMapper;
import com.seriuszg.medical.model.dto.FacilityDto;
import com.seriuszg.medical.model.dto.FacilityRegistrationDto;
import com.seriuszg.medical.model.entity.Facility;
import com.seriuszg.medical.repositories.FacilityRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class FacilityService {

    private final FacilityRepository facilityRepository;
    private final FacilityMapper facilityMapper;

    @Transactional
    public FacilityDto saveFacility (FacilityRegistrationDto facilityRegistrationDto) {
        log.trace("Checking if all required fields are filled");
        if (!facilityRegistrationDto.isCorrect()) {
            throw new RequiredFieldsNotFilledException();
        }
        if (facilityRepository.findByName(facilityRegistrationDto.getName()).isPresent()) {
            throw new FacilityAlreadyExistsException();
        }
        Facility facility = facilityMapper.registrationDtoToEntity(facilityRegistrationDto);
        log.trace("Saving the facility");
        return facilityMapper.toDto(facilityRepository.save(facility));
    }

    @Transactional
    public FacilityDto deleteFacility (Long id) {
        Facility facility = getFacilityById(id);
        log.trace("Facility has been found");
        facilityRepository.delete(facility);
        log.info("Facility has been removed");
        return facilityMapper.toDto(facility);
    }

    public List<FacilityDto> getAllFacilities() {
        log.info("Fetching all facilities");
        return facilityRepository.findAll().stream()
                .map(facilityMapper::toDto)
                .collect(Collectors.toList());
    }

    private Facility getFacilityById (Long id) {
        log.debug("Searching for the facility");
        return facilityRepository.findById(id).orElseThrow( () -> new EntityNotFoundException("Nie znaleziono placówki o podanm ID"));
    }
}
