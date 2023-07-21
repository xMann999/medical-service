package com.seriuszg.medical.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.seriuszg.medical.model.dto.PatientEditDto;
import com.seriuszg.medical.model.dto.PatientDto;
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

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class PatientControllerTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    PatientRepository patientRepository;
    @Autowired
    VisitRepository visitRepository;
    @Autowired
    private DataSource database;
    public static boolean dataLoaded = false;
    @BeforeEach
    public void setup() throws SQLException {
        if(!this.dataLoaded) {
            dataLoaded = true;
            try (Connection con = database.getConnection()) {
                ScriptUtils.executeSqlScript(con, new ClassPathResource("data.sql"));

            }
        }
    }

    @Test
    @Rollback
    void createPatient_DataCorrect_PatientCreated() throws Exception {
        PatientDto patient = createPatientDto("ddd1@gmail.com");
        mockMvc.perform(MockMvcRequestBuilders.post("/patients")
                        .content(objectMapper.writeValueAsString(patient))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("ddd1@gmail.com"));
    }

    @Test
    @Rollback
    void getPatient_PatientFound_PatientReturned() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/patients/sg@gmail.com"))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("sg@gmail.com"));
    }

    @Test
    @Rollback
    void showAllPatients_PatientsFound_PatientsReturned() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/patients"))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()").value(3))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].email").value("ssgg@gmail.com"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].email").value("sg@gmail.com"));
    }

    @Test
    @Rollback
    void deletePatient_PatientFound_PatientDeleted() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/patients/sg@gmail.com"))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("sg@gmail.com"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @Rollback
    void editPatientDetails_DataCorrect_DetailsChanged() throws Exception {
        PatientEditDto patientEditDto = new PatientEditDto("Jan", "Kowalski", "77711", "222@gmail.com");
        mockMvc.perform(MockMvcRequestBuilders.patch("/patients/sg@gmail.com/details")
                        .content(objectMapper.writeValueAsString(patientEditDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("222@gmail.com"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value("Jan"));
    }

    @Test
    @Rollback
    void editPatientPassword_PatientFound_PasswordChanged() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.patch("/patients/test1@gmail.com/password")
                        .content("2225")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isString())
                .andExpect(MockMvcResultMatchers.jsonPath("$").value("Pomyślnie zmieniono hasło"));
    }

    @Test
    @Rollback
    void getAllAssignedVisitsToSpecificPatient_PatientFound_VisitsReturned() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/patients/ssgg@gmail.com/visits"))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()").value(1));
    }

    private PatientDto createPatientDto(String email) {
        return PatientDto.builder()
                .email(email)
                .firstName("gg")
                .lastName("ee")
                .build();
    }

}
