package com.seriuszg.medical.mapper;

import com.seriuszg.medical.model.dto.FacilityDto;
import com.seriuszg.medical.model.dto.FacilityRegistrationDto;
import com.seriuszg.medical.model.entity.Facility;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, unmappedSourcePolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface FacilityMapper {

    FacilityDto toDto (Facility facility);

    Facility registrationDtoToEntity (FacilityRegistrationDto facilityRegistrationDto);
}
