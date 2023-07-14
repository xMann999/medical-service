package com.seriuszg.medical.mapper;

import com.seriuszg.medical.model.dto.PatientDto;
import com.seriuszg.medical.model.entity.Patient;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;


@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, unmappedSourcePolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface PatientMapper {

    PatientDto toDto(Patient patient);

    Patient toEntity(PatientDto patientDTO);
}
