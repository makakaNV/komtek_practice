package com.lab.mapper;

import com.lab.dto.request.PatientRequestDTO;
import com.lab.dto.response.PatientResponseDTO;
import com.lab.entity.Patient;

import java.util.List;

public interface PatientMapper {
    PatientResponseDTO toResponseDTO(Patient patient);
    List<PatientResponseDTO> toResponseDTOList(List<Patient> patients);
    Patient toEntity(PatientRequestDTO dto);
}
