package com.seriuszg.medical.mapper;

import com.seriuszg.medical.model.dto.PatientDTO;
import com.seriuszg.medical.model.entity.Patient;
import java.time.LocalDate;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-07-03T17:51:49+0200",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 20.0.1 (Oracle Corporation)"
)
@Component
public class PatientMapperImpl implements PatientMapper {

    @Override
    public PatientDTO toPatientDto(Patient patient) {
        if ( patient == null ) {
            return null;
        }

        String email = null;
        String password = null;
        String idCardNo = null;
        String firstName = null;
        String lastName = null;
        String phoneNumber = null;
        LocalDate birthday = null;

        email = patient.getEmail();
        password = patient.getPassword();
        idCardNo = patient.getIdCardNo();
        firstName = patient.getFirstName();
        lastName = patient.getLastName();
        phoneNumber = patient.getPhoneNumber();
        birthday = patient.getBirthday();

        PatientDTO patientDTO = new PatientDTO( email, password, idCardNo, firstName, lastName, phoneNumber, birthday );

        return patientDTO;
    }

    @Override
    public Patient toPatient(PatientDTO patientDTO) {
        if ( patientDTO == null ) {
            return null;
        }

        Patient patient = new Patient();

        patient.setEmail( patientDTO.getEmail() );
        patient.setPassword( patientDTO.getPassword() );
        patient.setIdCardNo( patientDTO.getIdCardNo() );
        patient.setFirstName( patientDTO.getFirstName() );
        patient.setLastName( patientDTO.getLastName() );
        patient.setPhoneNumber( patientDTO.getPhoneNumber() );
        patient.setBirthday( patientDTO.getBirthday() );

        return patient;
    }
}
