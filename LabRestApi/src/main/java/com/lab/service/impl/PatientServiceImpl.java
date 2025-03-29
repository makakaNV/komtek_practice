package com.lab.service.impl;

import com.lab.dto.request.PatientRequestDTO;
import com.lab.dto.response.OrderResponseDTO;
import com.lab.dto.response.PatientResponseDTO;
import com.lab.entity.Order;
import com.lab.entity.Patient;
import com.lab.exception.OrderNotFoundException;
import com.lab.exception.PatientNotFoundException;
import com.lab.mapper.OrderMapper;
import com.lab.mapper.PatientMapper;
import com.lab.repository.OrderRepository;
import com.lab.repository.PatientRepository;
import com.lab.service.PatientService;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.util.List;

@Service
public class PatientServiceImpl implements PatientService {

    private final PatientRepository patientRepository;
    private final OrderRepository orderRepository;
    private final PatientMapper patientMapper;
    private final OrderMapper orderMapper;

    public PatientServiceImpl(
            PatientRepository patientRepository,
            OrderRepository orderRepository,
            PatientMapper patientMapper,
            OrderMapper orderMapper
    ) {
        this.patientRepository = patientRepository;
        this.orderRepository = orderRepository;
        this.patientMapper = patientMapper;
        this.orderMapper = orderMapper;
    }

    @Override
    public List<PatientResponseDTO> getAllPatients(Pageable pageable) {
        Page<Patient> patientsPage = patientRepository.findAll(pageable);

        if (patientsPage.isEmpty()) {
            throw new PatientNotFoundException("Пациентов не найдено");
        }

        return patientMapper.toResponseDTOList(patientsPage.getContent());
    }

    @Override
    public PatientResponseDTO createPatient(PatientRequestDTO patientDTO) {
        Patient patient = patientMapper.toEntity(patientDTO);
        patient = patientRepository.save(patient);
        return patientMapper.toResponseDTO(patient);
    }

    @Override
    public PatientResponseDTO updatePatient(Long id, PatientRequestDTO patientDTO) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new PatientNotFoundException("Пациентов с id-" + id + " не найдено"));

        patient.setLastName(patientDTO.getLastName());
        patient.setFirstName(patientDTO.getFirstName());
        patient.setMiddleName(patientDTO.getMiddleName());
        patient.setBirthDate(patientDTO.getBirthDate());
        patient.setGender(patientDTO.getGender());
        patient.setPhoneNumber(patientDTO.getPhoneNumber());
        patient.setSnils(patientDTO.getSnils());

        patient = patientRepository.save(patient);

        return patientMapper.toResponseDTO(patient);
    }

    @Override
    public PatientResponseDTO getPatient(Long id) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new PatientNotFoundException("Пациентов с id-" + id + " не найдено"));
        return patientMapper.toResponseDTO(patient);
    }

    @Override
    public List<OrderResponseDTO> getPatientOrders(Long patientId) {
        List<Order> orders = orderRepository.findByPatientId(patientId);
        if (orders.isEmpty()) {
            throw new OrderNotFoundException("Заявок у пациента с id-" + patientId + " не найдено");
        }
        return orderMapper.toResponseDTOList(orders);
    }

    @Override
    public List<PatientResponseDTO> searchPatients(String lastName, String firstName, String middleName) {
        List<Patient> patients = patientRepository.findByLastNameAndFirstNameAndMiddleName(
                lastName,
                firstName,
                middleName
        );
        if (patients.isEmpty()) {
            throw new PatientNotFoundException("Пациентов с таким ФИО не найдено");
        }
        return patientMapper.toResponseDTOList(patients);
    }

    @Override
    public List<PatientResponseDTO> searchPatientsByBirthDate(LocalDate birthDate) {
        List<Patient> patients = patientRepository.findByBirthDate(birthDate);
        if (patients.isEmpty()) {
            throw new PatientNotFoundException("Пациентов с датой рождения " + birthDate + " не найдено");
        }
        return patientMapper.toResponseDTOList(patients);
    }

    @Override
    public List<PatientResponseDTO> searchPatients(
            String lastName,
            String firstName,
            String middleName,
            LocalDate birthDate
    ) {
        List<Patient> patients;

        if (birthDate != null && lastName != null && firstName != null && middleName != null) {
            patients = patientRepository.findByLastNameAndFirstNameAndMiddleNameAndBirthDate(
                    lastName, firstName, middleName, birthDate);
        } else if (birthDate != null) {
            patients = patientRepository.findByBirthDate(birthDate);
        } else if (lastName != null || firstName != null || middleName != null) {
            patients = patientRepository.findByLastNameAndFirstNameAndMiddleName(lastName, firstName, middleName);
        } else {
            throw new IllegalArgumentException("Необходимо указать хотя бы один параметр поиска");
        }

        if (patients.isEmpty()) {
            throw new PatientNotFoundException("Пациенты по указанным критериям не найдены");
        }

        return patientMapper.toResponseDTOList(patients);
    }

    @Override
    public void deletePatient(Long id) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new PatientNotFoundException("Пациентов с id-" + id + " не найдено"));
        patientRepository.delete(patient);
    }
}
