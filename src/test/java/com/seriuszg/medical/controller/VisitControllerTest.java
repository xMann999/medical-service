package com.seriuszg.medical.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.seriuszg.medical.model.dto.VisitRequest;
import com.seriuszg.medical.repositories.PatientRepository;
import com.seriuszg.medical.repositories.VisitRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class VisitControllerTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    VisitRepository visitRepository;
    @Autowired
    PatientRepository patientRepository;

    @Test
    @Rollback
    void createVisit_DataCorrect_VisitCreated() throws Exception {
        VisitRequest visitRequest = new VisitRequest(LocalDateTime.of(2024,3,10,15,00,0), Duration.parse("PT30M"));
        mockMvc.perform(MockMvcRequestBuilders.post("/visits")
                .content(objectMapper.writeValueAsString(visitRequest))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.visitEndTime").value("2024-03-10T15:30:00"));
    }

    @Test
    @Rollback
    void assignPatientToVisit_PatientAndVisitFound_PatientAssigned() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.patch("/visits/2/assignPatient")
                .content(objectMapper.writeValueAsString(1L))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.patientId").value(1L));
    }
}
