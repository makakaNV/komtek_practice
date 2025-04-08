package com.lab.service.impl;

import com.lab.cache.impl.CacheServiceImpl;
import com.lab.dto.request.PatientRequestDTO;
import com.lab.dto.response.OrderResponseDTO;
import com.lab.dto.response.PatientResponseDTO;
import com.lab.entity.Order;
import com.lab.entity.Patient;
import com.lab.entity.Test;
import com.lab.exception.OrderNotFoundException;
import com.lab.exception.PatientNotFoundException;
import com.lab.mapper.impl.OrderMapperImpl;
import com.lab.mapper.impl.PatientMapperImpl;
import com.lab.repository.OrderRepository;
import com.lab.repository.PatientRepository;
import com.lab.repository.TestRepository;
import com.lab.service.PatientService;
import jakarta.transaction.Transactional;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.util.List;

@Service
public class PatientServiceImpl implements PatientService {

    private final PatientRepository patientRepository;
    private final OrderRepository orderRepository;
    private final TestRepository testRepository;
    private final PatientMapperImpl patientMapperImpl;
    private final OrderMapperImpl orderMapperImpl;
    private final CacheServiceImpl cacheServiceImpl;

    public PatientServiceImpl(
            PatientRepository patientRepository,
            OrderRepository orderRepository,
            PatientMapperImpl patientMapperImpl,
            OrderMapperImpl orderMapperImpl,
            CacheServiceImpl patientCacheServiceImpl,
            TestRepository testRepository
    ) {
        this.patientRepository = patientRepository;
        this.orderRepository = orderRepository;
        this.patientMapperImpl = patientMapperImpl;
        this.orderMapperImpl = orderMapperImpl;
        this.cacheServiceImpl = patientCacheServiceImpl;
        this.testRepository = testRepository;
    }

    @Override
    public Page<PatientResponseDTO> getAllPatients(Pageable pageable) {
        Page<Patient> patientsPage = patientRepository.findAll(pageable);

        if (patientsPage.isEmpty()) {
            throw new PatientNotFoundException("Пациентов не найдено");
        }

        return patientsPage.map(patientMapperImpl::toResponseDTO);
    }

    @Override
    public PatientResponseDTO createPatient(PatientRequestDTO patientDTO) {
        Patient patient = patientMapperImpl.toEntity(patientDTO);
        patient = patientRepository.save(patient);
        return patientMapperImpl.toResponseDTO(patient);
    }

    @Override
    @CachePut(value = "patients", key = "#id")
    public PatientResponseDTO updatePatient(Long id, PatientRequestDTO patientDTO) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new PatientNotFoundException("Пациентов с id-" + id + " не найдено"));

        cacheServiceImpl.evictPatientCaches(patient);

        patient.setLastName(patientDTO.getLastName());
        patient.setFirstName(patientDTO.getFirstName());
        patient.setMiddleName(patientDTO.getMiddleName());
        patient.setBirthDate(patientDTO.getBirthDate());
        patient.setGender(patientDTO.getGender());
        patient.setPhoneNumber(patientDTO.getPhoneNumber());
        patient.setSnils(patientDTO.getSnils());

        patient = patientRepository.save(patient);

        return patientMapperImpl.toResponseDTO(patient);
    }

    @Override
    @Cacheable(value = "patients", key = "#id")
    public PatientResponseDTO getPatient(Long id) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new PatientNotFoundException("Пациентов с id-" + id + " не найдено"));
        return patientMapperImpl.toResponseDTO(patient);
    }

    @Override
    public List<OrderResponseDTO> getPatientOrders(Long patientId) {
        List<Order> orders = orderRepository.findByPatientId(patientId);
        if (orders.isEmpty()) {
            throw new OrderNotFoundException("Заявок у пациента с id-" + patientId + " не найдено");
        }
        return orderMapperImpl.toResponseDTOList(orders);
    }

    @Override
    @Cacheable(value = "patientsByFio", key = "T(java.util.Objects).hash(#lastName, #firstName, #middleName)")
    public List<PatientResponseDTO> searchPatients(String lastName, String firstName, String middleName) {
        List<Patient> patients = patientRepository.findByLastNameAndFirstNameAndMiddleName(
                lastName,
                firstName,
                middleName
        );
        if (patients.isEmpty()) {
            throw new PatientNotFoundException("Пациентов с таким ФИО не найдено");
        }
        return patientMapperImpl.toResponseDTOList(patients);
    }

    @Override
    @Cacheable(value = "patientsByBirthDate", key = "#birthDate")
    public List<PatientResponseDTO> searchPatientsByBirthDate(LocalDate birthDate) {
        List<Patient> patients = patientRepository.findByBirthDate(birthDate);
        if (patients.isEmpty()) {
            throw new PatientNotFoundException("Пациентов с датой рождения " + birthDate + " не найдено");
        }
        return patientMapperImpl.toResponseDTOList(patients);
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

        return patientMapperImpl.toResponseDTOList(patients);
    }

    @Override
    @CacheEvict(value = "patients", key = "#id")
    @Transactional
    public void deletePatient(Long id) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new PatientNotFoundException("Пациентов с id-" + id + " не найдено"));

        List<Order> orders = orderRepository.findByPatientId(patient.getId());
        orders.forEach(order -> {
            List<Test> tests = testRepository.findByOrderId(order.getId());
            tests.forEach(test -> {
                cacheServiceImpl.evictTestCaches(test);
                testRepository.delete(test);
            });
            cacheServiceImpl.evictOrderCaches(order);
            orderRepository.delete(order);
        });

        cacheServiceImpl.evictPatientCaches(patient);
        patientRepository.delete(patient);
    }

}
