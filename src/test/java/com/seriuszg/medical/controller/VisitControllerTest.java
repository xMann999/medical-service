package com.seriuszg.medical.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.seriuszg.medical.model.dto.VisitRequest;
import com.seriuszg.medical.repositories.PatientRepository;
import com.seriuszg.medical.repositories.VisitRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
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

    @Autowired
    private DataSource database;
    @BeforeEach
    public void setup() throws SQLException {
        if(!PatientControllerTest.dataLoaded) {
            PatientControllerTest.dataLoaded = true;
            try (Connection con = database.getConnection()) {
                ScriptUtils.executeSqlScript(con, new ClassPathResource("data.sql"));
            }
        }
    }

    @Test
    @Rollback
    void createVisit_DataCorrect_VisitCreated() throws Exception {
        VisitRequest visitRequest = new VisitRequest(LocalDateTime.of(2023,12,05,15,00,0), Duration.parse("PT30M"));
        mockMvc.perform(MockMvcRequestBuilders.post("/visits")
                .content(objectMapper.writeValueAsString(visitRequest))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.visitEndTime").value("2023-12-05T15:30:00"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(3L));
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
