package com.seriuszg.medical.mapper;

import com.seriuszg.medical.model.dto.VisitRequest;
import com.seriuszg.medical.model.dto.VisitResponse;
import com.seriuszg.medical.model.entity.Visit;
import org.springframework.stereotype.Service;

@Service
public class VisitMapper {

    public Visit requestToEntity(VisitRequest visitRequest) {
        return Visit.builder()
                .visitStartTime(visitRequest.getVisitStartTime())
                .visitEndTime(visitRequest.getVisitStartTime().plusMinutes(visitRequest.getDuration()))
                .duration(visitRequest.getDuration())
                .build();
    }

    public VisitResponse entityToResponse(Visit visit) {
        return VisitResponse.builder()
                .id(visit.getId())
                .visitStartTime(visit.getVisitStartTime())
                .visitEndTime(visit.getVisitEndTime())
                .patient(visit.getPatient())
                .build();
    }
}
