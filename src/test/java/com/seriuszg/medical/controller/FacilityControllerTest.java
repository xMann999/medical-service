package com.seriuszg.medical.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.seriuszg.medical.model.dto.FacilityRegistrationDto;
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
public class FacilityControllerTest {

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
    void saveFacility_DataCorrect_FacilitySaved() throws Exception {
        FacilityRegistrationDto facilityRegistrationDto = new FacilityRegistrationDto("Szpital9", "Lodz", "91-009", "Piłsudskiego", "91");
        mockMvc.perform(MockMvcRequestBuilders.post("/facilities")
                .content(objectMapper.writeValueAsString(facilityRegistrationDto))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(2L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Szpital9"));
    }

    @Test
    @Rollback
    void deleteFacility_FacilityFound_FacilityDeleted() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/facilities/1"))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("SZPITAL"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.city").value("Łódź"));
    }

    @Test
    @Rollback
    void showAllFacilities_FacilitiesFound_FacilitiesReturned() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/facilities"))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("SZPITAL"));
    }
}
