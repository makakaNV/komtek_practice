package com.lab.service;

import com.lab.dto.request.PatientRequestDTO;
import com.lab.dto.response.OrderResponseDTO;
import com.lab.dto.response.PatientResponseDTO;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface PatientService {
    List<PatientResponseDTO> getAllPatients(Pageable pageable);
    PatientResponseDTO createPatient(PatientRequestDTO patientDTO);
    PatientResponseDTO updatePatient(Long id, PatientRequestDTO patientDTO);
    PatientResponseDTO getPatient(Long id);
    List<PatientResponseDTO> searchPatients(String lastName, String firstName, String middleName);
    List<OrderResponseDTO> getPatientOrders(Long patientId);
    List<PatientResponseDTO> searchPatientsByBirthDate(LocalDate birthDate);
    List<PatientResponseDTO> searchPatients(String lastName, String firstName, String middleName, LocalDate birthDate);
    void deletePatient(Long id);
}
