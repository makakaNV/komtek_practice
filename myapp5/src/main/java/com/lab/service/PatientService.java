package com.lab.service;

import com.lab.dto.OrderDTO;
import com.lab.dto.PatientDTO;
import com.lab.entity.Order;
import com.lab.entity.Patient;
import com.lab.exception.OrderNotFoundException;
import com.lab.exception.PatientNotFoundException;
import com.lab.repository.OrderRepository;
import com.lab.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PatientService {

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private OrderRepository orderRepository;

    public List<PatientDTO> getAllPatients() {
        List<Patient> patients = patientRepository.findAll();
        if (patients.isEmpty()) {
            throw new PatientNotFoundException("Пациентов не найдено");
        }
        return patients.stream()
                .map(patient -> new PatientDTO(patient))
                .collect(Collectors.toList());
    }

    public PatientDTO getPatient(Long id) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new PatientNotFoundException("Пациентов с id-" + id + " не найдено"));
        return new PatientDTO(patient);
    }

    public PatientDTO createPatient(PatientDTO patientDTO) {
        Patient patient = new Patient(
                null,
                patientDTO.getLastName(),
                patientDTO.getFirstName(),
                patientDTO.getMiddleName(),
                patientDTO.getBirthDate(),
                patientDTO.getGender(),
                patientDTO.getPhoneNumber(),
                null
        );
        patient = patientRepository.save(patient);
        return new PatientDTO(patient);
    }

    public PatientDTO updatePatient(Long id, PatientDTO patientDTO) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new PatientNotFoundException("Пациентов с id-" + id + " не найдено"));

        patient.setLastName(patientDTO.getLastName());
        patient.setFirstName(patientDTO.getFirstName());
        patient.setMiddleName(patientDTO.getMiddleName());
        patient.setBirthDate(patientDTO.getBirthDate());
        patient.setGender(patientDTO.getGender());
        patient.setPhoneNumber(patientDTO.getPhoneNumber());

        patient = patientRepository.save(patient);
        return new PatientDTO(patient);
    }

    public void deletePatient(Long id) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new PatientNotFoundException("Пациентов с id-" + id + " не найдено"));
        patientRepository.delete(patient);
    }

    public List<PatientDTO> searchPatients(String lastName, String firstName, String middleName) {
        List<Patient> patients = patientRepository.findByLastNameAndFirstNameAndMiddleName(lastName, firstName, middleName);
        if (patients.isEmpty()) {
            throw new PatientNotFoundException("Пациентов с таким ФИО не найдено");
        }
        return patients.stream()
                .map(patient -> new PatientDTO(patient))
                .collect(Collectors.toList());
    }

    public List<OrderDTO> getPatientOrders(Long patientId) {
        List<Order> orders = orderRepository.findByPatientId(patientId);
        if (orders.isEmpty()) {
            throw new OrderNotFoundException("Заявок у пациента с id-" + patientId + " не найдено");
        }
        return orders.stream()
                .map(order -> new OrderDTO(order))
                .collect(Collectors.toList());
    }

    public List<PatientDTO> searchPatientsByBirthDate(LocalDate birthDate) {
        List<Patient> patients = patientRepository.findByBirthDate(birthDate);
        if (patients.isEmpty()) {
            throw new PatientNotFoundException("Пациентов с датой рождения " + birthDate + " не найдено");
        }
        return patients.stream()
                .map(patient -> new PatientDTO(patient))
                .collect(Collectors.toList());
    }

}
