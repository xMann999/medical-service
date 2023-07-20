package com.seriuszg.medical.mapper;

import com.seriuszg.medical.model.dto.DoctorDto;
import com.seriuszg.medical.model.dto.DoctorRegistrationDto;
import com.seriuszg.medical.model.entity.Doctor;
import com.seriuszg.medical.model.entity.Facility;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, unmappedSourcePolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface DoctorMapper {

    @Mapping(source = "facility", target = "facilityId", qualifiedByName = "mapToFacilityId")
    DoctorDto toDto (Doctor doctor);

    Doctor toEntity (DoctorDto doctorDto);

    Doctor registrationDtoToEntity (DoctorRegistrationDto doctorRegistrationDto);

    @Named("mapToFacilityId")
    default Long mapToFacilityId (Facility facility) {
        if (facility == null) {
            return null;
        }
        return facility.getId();
    }
}
