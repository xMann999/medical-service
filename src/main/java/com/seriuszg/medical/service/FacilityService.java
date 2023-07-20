package com.seriuszg.medical.service;

import com.seriuszg.medical.exceptions.EntityNotFoundException;
import com.seriuszg.medical.exceptions.FacilityAlreadyExistsException;
import com.seriuszg.medical.exceptions.RequiredFieldsNotFilledException;
import com.seriuszg.medical.mapper.FacilityMapper;
import com.seriuszg.medical.model.dto.FacilityDto;
import com.seriuszg.medical.model.dto.FacilityRegistrationDto;
import com.seriuszg.medical.model.entity.Facility;
import com.seriuszg.medical.repositories.FacilityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FacilityService {

    private final FacilityRepository facilityRepository;
    private final FacilityMapper facilityMapper;

    public FacilityDto saveFacility (FacilityRegistrationDto facilityRegistrationDto) {
        if (!facilityRegistrationDto.isCorrect()) {
            throw new RequiredFieldsNotFilledException();
        }
        if (facilityRepository.findByName(facilityRegistrationDto.getName()).isPresent()) {
            throw new FacilityAlreadyExistsException();
        }
        Facility facility = facilityMapper.registrationDtoToEntity(facilityRegistrationDto);
        return facilityMapper.toDto(facilityRepository.save(facility));
    }

    public FacilityDto deleteFacility (Long id) {
        Facility facility = getFacilityById(id);
        facilityRepository.delete(facility);
        return facilityMapper.toDto(facility);
    }

    public List<FacilityDto> getAllFacilities() {
        return facilityRepository.findAll().stream()
                .map(facilityMapper::toDto)
                .collect(Collectors.toList());
    }

    private Facility getFacilityById (Long id) {
        return facilityRepository.findById(id).orElseThrow( () -> new EntityNotFoundException("Nie znaleziono placówki o podanm ID"));
    }
}
