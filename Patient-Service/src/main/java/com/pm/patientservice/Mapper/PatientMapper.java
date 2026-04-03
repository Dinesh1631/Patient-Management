package com.pm.patientservice.Mapper;

import com.pm.patientservice.DTO.PatientRequestDTO;
import com.pm.patientservice.DTO.PatientResponceDTO;
import com.pm.patientservice.Modal.Patient;

import java.time.LocalDate;

public class PatientMapper {

    public static PatientResponceDTO EntityTODtoMapper(Patient patient) {
        PatientResponceDTO dto = new PatientResponceDTO();
        dto.setId(String.valueOf(patient.getId()));
        dto.setName(patient.getName());
        dto.setAddress(patient.getAddress());
        dto.setEmail(patient.getEmail());
        dto.setDateOfBirth(patient.getDateOfBirth().toString());
        return dto;
    }

    public static Patient DtoTOEntityMapper(PatientRequestDTO patientRequestDTO) {
        Patient patient = new Patient();
        patient.setAddress(patientRequestDTO.getAddress());
        patient.setEmail(patientRequestDTO.getEmail());
        patient.setName(patientRequestDTO.getName());
        patient.setDateOfBirth(LocalDate.parse(patientRequestDTO.getDateOfBirth()));
        patient.setRegisteredDate(LocalDate.parse(patientRequestDTO.getRegisteredDate()));
        return patient;
    }
}
