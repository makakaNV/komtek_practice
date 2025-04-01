package com.lab.mapper.impl;

import com.lab.dto.request.PatientRequestDTO;
import com.lab.dto.response.PatientResponseDTO;
import com.lab.entity.Patient;
import com.lab.mapper.PatientMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class PatientMapperImpl implements PatientMapper {

    public PatientResponseDTO toResponseDTO(Patient patient) {
        return PatientResponseDTO.builder()
                .id(patient.getId())
                .lastName(patient.getLastName())
                .firstName(patient.getFirstName())
                .middleName(patient.getMiddleName())
                .birthDate(patient.getBirthDate())
                .gender(patient.getGender())
                .phoneNumber(patient.getPhoneNumber())
                .snils(patient.getSnils())
                .build();
    }

    public List<PatientResponseDTO> toResponseDTOList(List<Patient> patients) {
        return patients.stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    public Patient toEntity(PatientRequestDTO dto) {
        return Patient.builder()
                .lastName(dto.getLastName())
                .firstName(dto.getFirstName())
                .middleName(dto.getMiddleName())
                .birthDate(dto.getBirthDate())
                .gender(dto.getGender())
                .phoneNumber(dto.getPhoneNumber())
                .snils(dto.getSnils())
                .build();
    }
}

