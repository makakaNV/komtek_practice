package com.lab.service;

import com.lab.dto.request.PatientRequestDTO;
import com.lab.dto.response.OrderResponseDTO;
import com.lab.dto.response.PatientResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public interface PatientService {
    Page<PatientResponseDTO> getAllPatients(Pageable pageable);
    PatientResponseDTO createPatient(PatientRequestDTO patientDTO);
    PatientResponseDTO updatePatient(Long id, PatientRequestDTO patientDTO);
    PatientResponseDTO getPatient(Long id);
    List<PatientResponseDTO> searchPatients(String lastName, String firstName, String middleName);
    List<OrderResponseDTO> getPatientOrders(Long patientId);
    List<PatientResponseDTO> searchPatientsByBirthDate(LocalDate birthDate);
    List<PatientResponseDTO> searchPatients(String lastName, String firstName, String middleName, LocalDate birthDate);
    void deletePatient(Long id);
}
