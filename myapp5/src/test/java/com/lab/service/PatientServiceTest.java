package com.lab.service;

import com.lab.dto.OrderDTO;
import com.lab.dto.PatientDTO;
import com.lab.entity.Gender;
import com.lab.entity.Order;
import com.lab.entity.Patient;
import com.lab.entity.Status;
import com.lab.exception.OrderNotFoundException;
import com.lab.exception.PatientNotFoundException;
import com.lab.repository.OrderRepository;
import com.lab.repository.PatientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class PatientServiceTest {

    @Mock
    private PatientRepository patientRepository;

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private PatientService patientService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllPatients_ShouldReturnListOfPatientDTOs() {
        Patient patient = new Patient(1L, "Иванов", "Иван", "Иванович", LocalDate.of(1990, 1, 1), Gender.MALE, null, null);
        when(patientRepository.findAll()).thenReturn(List.of(patient));

        List<PatientDTO> result = patientService.getAllPatients();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Иванов", result.get(0).getLastName());
        verify(patientRepository, times(1)).findAll();
    }

    @Test
    void getPatient_WhenPatientExists_ShouldReturnPatientDTO() {
        Long patientId = 1L;
        Patient patient = new Patient(patientId, "Иванов", "Иван", "Иванович", LocalDate.of(1990, 1, 1), Gender.MALE, null, null);
        when(patientRepository.findById(patientId)).thenReturn(Optional.of(patient));

        PatientDTO result = patientService.getPatient(patientId);

        assertNotNull(result);
        assertEquals(patientId, result.getId());
        assertEquals("Иванов", result.getLastName());
        verify(patientRepository, times(1)).findById(patientId);
    }

    @Test
    void getPatient_WhenPatientDoesNotExist_ShouldThrowPatientNotFoundException() {
        Long patientId = 999L;
        when(patientRepository.findById(patientId)).thenReturn(Optional.empty());

        assertThrows(PatientNotFoundException.class, () -> patientService.getPatient(patientId));
        verify(patientRepository, times(1)).findById(patientId);
    }

    @Test
    void createPatient_ShouldReturnCreatedPatientDTO() {
        PatientDTO patientDTO = new PatientDTO(null, "Иванов", "Иван", "Иванович", LocalDate.of(1990, 1, 1), Gender.MALE, "+7932010203");
        Patient savedPatient = new Patient(1L, "Иванов", "Иван", "Иванович", LocalDate.of(1990, 1, 1), Gender.MALE, "+7932010203", null);
        when(patientRepository.save(any(Patient.class))).thenReturn(savedPatient);

        PatientDTO result = patientService.createPatient(patientDTO);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Иванов", result.getLastName());
        verify(patientRepository, times(1)).save(any(Patient.class));
    }

    @Test
    void updatePatient_WhenPatientExists_ShouldReturnUpdatedPatientDTO() {
        Long patientId = 1L;
        Patient existingPatient = new Patient(patientId, "Иванов", "Иван", "Иванович", LocalDate.of(1990, 1, 1), Gender.MALE, "+7932010203", null);
        PatientDTO updatedPatientDTO = new PatientDTO(patientId, "Петров", "Петр", "Петрович", LocalDate.of(1985, 5, 15), Gender.MALE, "+7932010203");

        when(patientRepository.findById(patientId)).thenReturn(Optional.of(existingPatient));
        when(patientRepository.save(any(Patient.class))).thenReturn(existingPatient);

        PatientDTO result = patientService.updatePatient(patientId, updatedPatientDTO);

        assertNotNull(result);
        assertEquals("Петров", result.getLastName());
        verify(patientRepository, times(1)).findById(patientId);
        verify(patientRepository, times(1)).save(any(Patient.class));
    }

    @Test
    void updatePatient_WhenPatientDoesNotExist_ShouldThrowPatientNotFoundException() {
        Long patientId = 999L;
        PatientDTO updatedPatientDTO = new PatientDTO(patientId, "Петров", "Петр", "Петрович", LocalDate.of(1985, 5, 15), Gender.MALE, "+7932010203");
        when(patientRepository.findById(patientId)).thenReturn(Optional.empty());

        assertThrows(PatientNotFoundException.class, () -> patientService.updatePatient(patientId, updatedPatientDTO));
        verify(patientRepository, times(1)).findById(patientId);
    }

    @Test
    void deletePatient_WhenPatientExists_ShouldDeletePatient() {
        Long patientId = 1L;
        Patient patient = new Patient(patientId, "Иванов", "Иван", "Иванович", LocalDate.of(1990, 1, 1), Gender.MALE, null, null);
        when(patientRepository.findById(patientId)).thenReturn(Optional.of(patient));

        patientService.deletePatient(patientId);

        verify(patientRepository, times(1)).delete(patient);
    }

    @Test
    void deletePatient_WhenPatientDoesNotExist_ShouldThrowPatientNotFoundException() {
        Long patientId = 999L;
        when(patientRepository.findById(patientId)).thenReturn(Optional.empty());

        assertThrows(PatientNotFoundException.class, () -> patientService.deletePatient(patientId));
        verify(patientRepository, times(1)).findById(patientId);
    }

    @Test
    void searchPatients_WhenPatientsExist_ShouldReturnListOfPatientDTOs() {
        String lastName = "Иванов";
        String firstName = "Иван";
        String middleName = "Иванович";
        Patient patient = new Patient(1L, lastName, firstName, middleName, LocalDate.of(1990, 1, 1), Gender.MALE, null, null);
        when(patientRepository.findByLastNameAndFirstNameAndMiddleName(lastName, firstName, middleName)).thenReturn(List.of(patient));

        List<PatientDTO> result = patientService.searchPatients(lastName, firstName, middleName);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Иванов", result.get(0).getLastName());
        verify(patientRepository, times(1)).findByLastNameAndFirstNameAndMiddleName(lastName, firstName, middleName);
    }

    @Test
    void searchPatients_WhenPatientsDoNotExist_ShouldThrowPatientNotFoundException() {
        String lastName = "Иванов";
        String firstName = "Иван";
        String middleName = "Иванович";
        when(patientRepository.findByLastNameAndFirstNameAndMiddleName(lastName, firstName, middleName)).thenReturn(Collections.emptyList());

        assertThrows(PatientNotFoundException.class, () -> patientService.searchPatients(lastName, firstName, middleName));
        verify(patientRepository, times(1)).findByLastNameAndFirstNameAndMiddleName(lastName, firstName, middleName);
    }

    @Test
    void getPatientOrders_WhenOrdersExist_ShouldReturnListOfOrderDTOs() {
        Long patientId = 1L;
        Patient patient = new Patient(patientId, "Иванов", "Иван", "Иванович", LocalDate.of(1990, 1, 1), Gender.MALE, null, null);
        Order order = new Order(1L, patient, LocalDateTime.now(), Status.REGISTERED, "Комментарий к заказу");
        when(orderRepository.findByPatientId(patientId)).thenReturn(List.of(order));

        List<OrderDTO> result = patientService.getPatientOrders(patientId);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Комментарий к заказу", result.get(0).getComment());
        assertEquals(patientId, result.get(0).getPatientId());
        verify(orderRepository, times(1)).findByPatientId(patientId);
    }

    @Test
    void getPatientOrders_WhenOrdersDoNotExist_ShouldThrowOrderNotFoundException() {
        Long patientId = 999L;
        when(orderRepository.findByPatientId(patientId)).thenReturn(Collections.emptyList());

        assertThrows(OrderNotFoundException.class, () -> patientService.getPatientOrders(patientId));
        verify(orderRepository, times(1)).findByPatientId(patientId);
    }

    @Test
    void searchPatientsByBirthDate_WhenPatientsExist_ShouldReturnListOfPatientDTOs() {
        LocalDate birthDate = LocalDate.of(1990, 1, 1);
        Patient patient = new Patient(1L, "Иванов", "Иван", "Иванович", birthDate, Gender.MALE, null, null);
        when(patientRepository.findByBirthDate(birthDate)).thenReturn(List.of(patient));

        List<PatientDTO> result = patientService.searchPatientsByBirthDate(birthDate);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Иванов", result.get(0).getLastName());
        verify(patientRepository, times(1)).findByBirthDate(birthDate);
    }

    @Test
    void searchPatientsByBirthDate_WhenPatientsDoNotExist_ShouldThrowPatientNotFoundException() {
        LocalDate birthDate = LocalDate.of(1990, 1, 1);
        when(patientRepository.findByBirthDate(birthDate)).thenReturn(Collections.emptyList());

        assertThrows(PatientNotFoundException.class, () -> patientService.searchPatientsByBirthDate(birthDate));
        verify(patientRepository, times(1)).findByBirthDate(birthDate);
    }
}
