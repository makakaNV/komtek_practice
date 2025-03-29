package com.lab.service;

import com.lab.dto.request.PatientRequestDTO;
import com.lab.dto.response.OrderResponseDTO;
import com.lab.dto.response.PatientResponseDTO;
import com.lab.entity.*;
import com.lab.exception.OrderNotFoundException;
import com.lab.exception.PatientNotFoundException;
import com.lab.mapper.OrderMapper;
import com.lab.mapper.PatientMapper;
import com.lab.repository.OrderRepository;
import com.lab.repository.PatientRepository;
import com.lab.service.impl.PatientServiceImpl;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;


@ExtendWith(MockitoExtension.class)
public class PatientServiceImplTest {

    @Mock
    private PatientRepository patientRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private PatientMapper patientMapper;

    @Mock
    private OrderMapper orderMapper;

    @InjectMocks
    private PatientServiceImpl patientService;

    private final Patient patient = new Patient(
            1L,
            "Иванов",
            "Иван",
            "Иванович",
            LocalDate.of(1970, 1, 1),
            Gender.MALE,
            "+79028518877",
            "123"
    );

    private final Order order = new Order(
            1L,
            patient,
            LocalDateTime.now(),
            Status.REGISTERED,
            "Комментарий"
    );

    private final PatientRequestDTO patientRequestDTO = new PatientRequestDTO(
            "Иванов",
            "Иван",
            "Иванович",
            LocalDate.of(1970, 1, 1),
            Gender.MALE,
            "+79028518877",
            "123"
    );

    private final PatientResponseDTO patientResponseDTO = new PatientResponseDTO(
            1L,
            "Иванов",
            "Иван",
            "Иванович",
            LocalDate.of(1970, 1, 1),
            Gender.MALE,
            "+79028518877",
            "123"
    );

    private final OrderResponseDTO orderResponseDTO = new OrderResponseDTO(
            1L,
            1L,
            LocalDateTime.now(),
            Status.REGISTERED,
            "Комментарий"
    );

    @Test
    void getAllPatients_ShouldReturnPatients_WhenPatientsExist() {
        Page<Patient> page = new PageImpl<>(List.of(patient));
        when(patientRepository.findAll(any(Pageable.class))).thenReturn(page);
        when(patientMapper.toResponseDTOList(anyList())).thenReturn(List.of(patientResponseDTO));

        List<PatientResponseDTO> result = patientService.getAllPatients(Pageable.unpaged());

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        verify(patientRepository).findAll(any(Pageable.class));
    }

    @Test
    void getAllPatients_ShouldThrowException_WhenNoPatientsFound() {
        when(patientRepository.findAll(any(Pageable.class))).thenReturn(Page.empty());

        assertThrows(PatientNotFoundException.class, () ->
                patientService.getAllPatients(Pageable.unpaged()));
    }

    @Test
    void createPatient_ShouldCreatePatient() {
        when(patientMapper.toEntity(any(PatientRequestDTO.class))).thenReturn(patient);
        when(patientRepository.save(any(Patient.class))).thenReturn(patient);
        when(patientMapper.toResponseDTO(any(Patient.class))).thenReturn(patientResponseDTO);

        PatientResponseDTO result = patientService.createPatient(patientRequestDTO);

        assertNotNull(result);
        assertEquals("Иван", result.getFirstName());
        verify(patientRepository).save(any(Patient.class));
    }

    @Test
    void updatePatient_ShouldUpdatePatient_WhenPatientExists() {
        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));
        when(patientRepository.save(any(Patient.class))).thenReturn(patient);
        when(patientMapper.toResponseDTO(any(Patient.class))).thenReturn(patientResponseDTO);

        PatientResponseDTO result = patientService.updatePatient(1L, patientRequestDTO);

        assertNotNull(result);
        verify(patientRepository).save(patient);
    }

    @Test
    void updatePatient_ShouldThrowException_WhenPatientNotFound() {
        when(patientRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(PatientNotFoundException.class, () ->
                patientService.updatePatient(1L, patientRequestDTO));
    }

    @Test
    void getPatient_ShouldReturnPatient_WhenPatientExists() {
        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));
        when(patientMapper.toResponseDTO(any(Patient.class))).thenReturn(patientResponseDTO);

        PatientResponseDTO result = patientService.getPatient(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void getPatient_ShouldThrowException_WhenPatientNotFound() {
        when(patientRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(PatientNotFoundException.class, () ->
                patientService.getPatient(1L));
    }

    @Test
    void getPatientOrders_ShouldReturnOrders_WhenPatientAndOrdersExist() {
        when(orderRepository.findByPatientId(1L)).thenReturn(List.of(order));
        when(orderMapper.toResponseDTOList(anyList())).thenReturn(List.of(orderResponseDTO));

        List<OrderResponseDTO> result = patientService.getPatientOrders(1L);

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
    }

    @Test
    void getPatientOrders_ShouldThrowException_WhenNoOrdersFound() {
        when(orderRepository.findByPatientId(1L)).thenReturn(Collections.emptyList());

        assertThrows(OrderNotFoundException.class, () ->
                patientService.getPatientOrders(1L));
    }

    @Test
    void searchPatients_ShouldReturnPatients_WhenFoundByFIO() {
        when(patientRepository.findByLastNameAndFirstNameAndMiddleName(
                "Иванов", "Иван", "Иванович"
        ))
                .thenReturn(List.of(patient));
        when(patientMapper.toResponseDTOList(anyList())).thenReturn(List.of(patientResponseDTO));

        List<PatientResponseDTO> result = patientService.searchPatients(
                "Иванов", "Иван", "Иванович"
        );

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
    }

    @Test
    void searchPatientsByBirthDate_ShouldReturnPatients_WhenFoundByBirthDate() {
        LocalDate birthDate = LocalDate.of(1970, 1, 1);
        when(patientRepository.findByBirthDate(birthDate)).thenReturn(List.of(patient));
        when(patientMapper.toResponseDTOList(anyList())).thenReturn(List.of(patientResponseDTO));

        List<PatientResponseDTO> result = patientService.searchPatientsByBirthDate(birthDate);

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
    }

    @Test
    void searchPatients_ShouldReturnPatients_WhenFoundByAllCriteria() {
        LocalDate birthDate = LocalDate.of(1970, 1, 1);
        when(patientRepository.findByLastNameAndFirstNameAndMiddleNameAndBirthDate(
                "Иванов", "Иван", "Иванович", birthDate))
                .thenReturn(List.of(patient));
        when(patientMapper.toResponseDTOList(anyList())).thenReturn(List.of(patientResponseDTO));

        List<PatientResponseDTO> result = patientService.searchPatients(
                "Иванов", "Иван", "Иванович", birthDate);

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
    }

    @Test
    void searchPatients_ShouldThrowException_WhenNoCriteriaProvided() {
        assertThrows(IllegalArgumentException.class, () ->
                patientService.searchPatients(null, null, null, null));
    }

    @Test
    void deletePatient_ShouldDeletePatient_WhenPatientExists() {
        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));
        doNothing().when(patientRepository).delete(any(Patient.class));

        patientService.deletePatient(1L);

        verify(patientRepository).delete(patient);
    }

    @Test
    void deletePatient_ShouldThrowException_WhenPatientNotFound() {
        when(patientRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(PatientNotFoundException.class, () ->
                patientService.deletePatient(1L));
    }
}