package com.seriuszg.medical.mapper;

import com.seriuszg.medical.model.dto.VisitRequest;
import com.seriuszg.medical.model.dto.VisitResponse;
import com.seriuszg.medical.model.entity.Patient;
import com.seriuszg.medical.model.entity.Visit;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, unmappedSourcePolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface VisitMapper {

    @Mapping(source = "patient", target = "patientId", qualifiedByName = "mapToPatientId")
    VisitResponse entityToResponse(Visit visit);

    @Mapping(target = "visitEndTime", expression = "java(mapToVisitEndTime(visitRequest))")
    Visit requestToEntity(VisitRequest visitRequest);

    @Named("mapToPatientId")
    default Long mapToPatientId(Patient patient) {
        if (patient == null) {
            return null;
        }
        return patient.getId();
    }

    default LocalDateTime mapToVisitEndTime(VisitRequest visitRequest) {
        return visitRequest.getVisitStartTime().plusMinutes(visitRequest.getDuration().toMinutes());
    }
}
