package com.seriuszg.medical.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.seriuszg.medical.model.dto.DoctorEditDto;
import com.seriuszg.medical.model.dto.DoctorRegistrationDto;
import com.seriuszg.medical.model.dto.Specialisation;
import com.seriuszg.medical.repositories.DoctorRepository;
import com.seriuszg.medical.repositories.FacilityRepository;
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
public class DoctorControllerTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    DoctorRepository doctorRepository;
    @Autowired
    FacilityRepository facilityRepository;

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
    void createDoctor_DataCorrect_DoctorCreated() throws Exception {
        DoctorRegistrationDto doctorRegistrationDto = new DoctorRegistrationDto("11@gmail.com", "eee", "Monika", "Królikowska", Specialisation.NEUROLOGY);
        mockMvc.perform(MockMvcRequestBuilders.post("/doctors")
                .content(objectMapper.writeValueAsString(doctorRegistrationDto))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(3L));
    }

    @Test
    @Rollback
    void getDoctor_DoctorFound_DoctorReturned() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/doctors/1"))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("222@gmail.com"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value("Janek"));
    }

    @Test
    @Rollback
    void deleteDoctor_DoctorFound_DoctorDeleted() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/doctors/1"))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("222@gmail.com"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value("Janek"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @Rollback
    void editDoctorDetails_DataCorrect_DoctorEdited() throws Exception {
        DoctorEditDto doctorEditDto = new DoctorEditDto("sgg@gmail.com","Sergiusz","Gołacki",Specialisation.ANESTHESIOLOGY);
        mockMvc.perform(MockMvcRequestBuilders.patch("/doctors/1/details")
                .content(objectMapper.writeValueAsString(doctorEditDto))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("sgg@gmail.com"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value("Sergiusz"));
    }

    @Test
    @Rollback
    void editDoctorPassword_DoctorFound_PasswordChanged() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.patch("/doctors/2/password")
                .content("nowehaslo1")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Pomyślnie zmieniono hasło"));
    }

    @Test
    @Rollback
    void assignDoctorToFacility_DoctorAndFacilityFound_DoctorAssigned() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.patch("/doctors/2/assign")
                .content("1")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.facilityId").value(1L));
    }
}
