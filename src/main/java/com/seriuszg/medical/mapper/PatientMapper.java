package com.seriuszg.medical.mapper;

import com.seriuszg.medical.model.dto.PatientDTO;
import com.seriuszg.medical.model.entity.Patient;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import java.util.*;



@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, unmappedSourcePolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface PatientMapper  {

    PatientDTO toPatientDto(Patient patient);
    Patient toPatient(PatientDTO patientDTO);


}
