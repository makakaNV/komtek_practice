package com.lab.service;

import com.lab.dto.TestDTO;
import com.lab.entity.*;
import com.lab.exception.OrderNotFoundException;
import com.lab.exception.TestNotFoundException;
import com.lab.exception.TestTypeNotFoundException;
import com.lab.repository.OrderRepository;
import com.lab.repository.TestRepository;
import com.lab.repository.TestTypeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class TestServiceTest {

    @Mock
    private TestRepository testRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private TestTypeRepository testTypeRepository;

    @InjectMocks
    private TestService testService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllTests_WhenTestsExist_ShouldReturnListOfTestDTOs() {
        Patient patient = new Patient(1L, "Иванов", "Иван", "Иванович", LocalDate.of(1990, 1, 1), Gender.MALE, null, null);
        Order order = new Order(1L, patient, LocalDateTime.now(), Status.REGISTERED, "Комментарий");
        TestType testType = new TestType(1L, "Анализ крови", "AN-001", "Описание анализа крови", new BigDecimal("100.00"));
        com.lab.entity.Test test = new com.lab.entity.Test(1L, order, testType, LocalDateTime.now(), "result", "refvalue", TestStatus.COMPLETED);

        when(testRepository.findAll()).thenReturn(List.of(test));

        List<TestDTO> result = testService.getAllTests();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("result", result.get(0).getResult());
        verify(testRepository, times(1)).findAll();
    }

    @Test
    void getAllTests_WhenTestsDoNotExist_ShouldReturnEmptyList() {
        when(testRepository.findAll()).thenReturn(Collections.emptyList());

        List<TestDTO> result = testService.getAllTests();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(testRepository, times(1)).findAll();
    }

    @Test
    void getTestById_WhenTestExists_ShouldReturnTestDTO() {
        Long testId = 1L;
        Patient patient = new Patient(1L, "Иванов", "Иван", "Иванович", LocalDate.of(1990, 1, 1), Gender.MALE, null, null);
        Order order = new Order(1L, patient, LocalDateTime.now(), Status.REGISTERED, "Комментарий");
        TestType testType = new TestType(1L, "Анализ крови", "code", "desc", new BigDecimal("100.00"));
        com.lab.entity.Test test = new com.lab.entity.Test(1L, order, testType, LocalDateTime.now(), "result", "refvalue", TestStatus.COMPLETED);

        when(testRepository.findById(testId)).thenReturn(Optional.of(test));

        TestDTO result = testService.getTestById(testId);

        assertNotNull(result);
        assertEquals(testId, result.getId());
        assertEquals("result", result.getResult());
        verify(testRepository, times(1)).findById(testId);
    }

    @Test
    void getTestById_WhenTestDoesNotExist_ShouldThrowTestNotFoundException() {
        Long testId = 999L;
        when(testRepository.findById(testId)).thenReturn(Optional.empty());

        assertThrows(TestNotFoundException.class, () -> testService.getTestById(testId));
        verify(testRepository, times(1)).findById(testId);
    }

    @Test
    void createTest_ShouldReturnCreatedTestDTO() {
        Long orderId = 1L;
        Long testTypeId = 1L;
        Patient patient = new Patient(1L, "Иванов", "Иван", "Иванович", LocalDate.of(1990, 1, 1), Gender.MALE, null, null);
        Order order = new Order(orderId, patient, LocalDateTime.now(), Status.REGISTERED, "Комментарий");
        TestType testType = new TestType(testTypeId, "Анализ крови", "code", "desc", new BigDecimal("100.00"));
        TestDTO testDTO = new TestDTO(null, orderId, testTypeId, LocalDateTime.now(), "result", "refvalue", TestStatus.COMPLETED);
        com.lab.entity.Test savedTest = new com.lab.entity.Test(1L, order, testType, LocalDateTime.now(), "result", "refvalue", TestStatus.COMPLETED);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(testTypeRepository.findById(testTypeId)).thenReturn(Optional.of(testType));
        when(testRepository.save(any(com.lab.entity.Test.class))).thenReturn(savedTest);


        TestDTO result = testService.createTest(testDTO);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("result", result.getResult());
        verify(orderRepository, times(1)).findById(orderId);
        verify(testTypeRepository, times(1)).findById(testTypeId);
        verify(testRepository, times(1)).save(any(com.lab.entity.Test.class));
    }

    @Test
    void createTest_WhenOrderDoesNotExist_ShouldThrowOrderNotFoundException() {
        Long orderId = 999L;
        Long testTypeId = 1L;
        TestDTO testDTO = new TestDTO(null, orderId, testTypeId, LocalDateTime.now(), "result", "refvalue", TestStatus.COMPLETED);
        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        assertThrows(OrderNotFoundException.class, () -> testService.createTest(testDTO));
        verify(orderRepository, times(1)).findById(orderId);
    }

    @Test
    void createTest_WhenTestTypeDoesNotExist_ShouldThrowTestTypeNotFoundException() {
        Long orderId = 1L;
        Long testTypeId = 999L;
        Order order = new Order(orderId, null, LocalDateTime.now(), Status.REGISTERED, "Комментарий");
        TestDTO testDTO = new TestDTO(null, orderId, testTypeId, LocalDateTime.now(), "result", "refvalue", TestStatus.COMPLETED);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(testTypeRepository.findById(testTypeId)).thenReturn(Optional.empty());

        assertThrows(TestTypeNotFoundException.class, () -> testService.createTest(testDTO));
        verify(orderRepository, times(1)).findById(orderId);
        verify(testTypeRepository, times(1)).findById(testTypeId);
    }

    @Test
    void updateTest_WhenTestExists_ShouldReturnUpdatedTestDTO() {
        Long testId = 1L;
        Order order = new Order(1L, null, LocalDateTime.now(), Status.REGISTERED, "Комментарий");
        TestType testType = new TestType(1L, "Анализ крови", "code", "desc", new BigDecimal("150.00"));
        com.lab.entity.Test existingTest = new com.lab.entity.Test(testId, order, testType, LocalDateTime.now(), "result1", "refvalue1", TestStatus.COMPLETED);
        TestDTO updatedTestDTO = new TestDTO(testId, 1L, 1L, LocalDateTime.now(), "result2", "refvalue2", TestStatus.COMPLETED);

        when(testRepository.findById(testId)).thenReturn(Optional.of(existingTest));
        when(testRepository.save(any(com.lab.entity.Test.class))).thenAnswer(invocation -> invocation.getArgument(0));

        TestDTO result = testService.updateTest(testId, updatedTestDTO);

        assertNotNull(result);
        assertEquals("result2", result.getResult());
        assertEquals("refvalue2", result.getReferenceValues());
        verify(testRepository, times(1)).findById(testId);
        verify(testRepository, times(1)).save(any(com.lab.entity.Test.class));
    }

    @Test
    void updateTest_WhenTestDoesNotExist_ShouldThrowTestNotFoundException() {
        Long testId = 999L;
        TestDTO updatedTestDTO = new TestDTO(testId, 1L, 1L, LocalDateTime.now(), "result", "refvalue", TestStatus.COMPLETED);
        when(testRepository.findById(testId)).thenReturn(Optional.empty());

        assertThrows(TestNotFoundException.class, () -> testService.updateTest(testId, updatedTestDTO));
        verify(testRepository, times(1)).findById(testId);
    }

    @Test
    void updateTestResult_WhenTestExists_ShouldReturnUpdatedTestDTO() {
        Long testId = 1L;
        Order order = new Order(1L, null, LocalDateTime.now(), Status.REGISTERED, "Комментарий");
        TestType testType = new TestType(1L, "Анализ крови", "code", "desc", new BigDecimal("150.00"));
        com.lab.entity.Test existingTest = new com.lab.entity.Test(testId, order, testType, LocalDateTime.now(), "Наличие антител", "refvalue", TestStatus.COMPLETED);
        String newResult = "Высокий уровень холестерина";
        when(testRepository.findById(testId)).thenReturn(Optional.of(existingTest));
        when(testRepository.save(any(com.lab.entity.Test.class))).thenAnswer(invocation -> invocation.getArgument(0));

        TestDTO result = testService.updateTestResult(testId, newResult);

        assertNotNull(result);
        assertEquals("Наличие антител, Высокий уровень холестерина", result.getResult());
        verify(testRepository, times(1)).findById(testId);
        verify(testRepository, times(1)).save(any(com.lab.entity.Test.class));
    }

    @Test
    void updateTestResult_WhenTestDoesNotExist_ShouldThrowTestNotFoundException() {
        Long testId = 999L;
        String newResult = "Высокий уровень холестерина";
        when(testRepository.findById(testId)).thenReturn(Optional.empty());

        assertThrows(TestNotFoundException.class, () -> testService.updateTestResult(testId, newResult));
        verify(testRepository, times(1)).findById(testId);
    }
}
