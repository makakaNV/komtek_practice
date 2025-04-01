package com.lab.service;

import com.lab.dto.request.TestRequestDTO;
import com.lab.dto.response.TestResponseDTO;
import com.lab.entity.*;
import com.lab.exception.OrderNotFoundException;
import com.lab.exception.TestNotFoundException;
import com.lab.exception.TestTypeNotFoundException;
import com.lab.mapper.impl.TestMapperImpl;
import com.lab.repository.OrderRepository;
import com.lab.repository.TestRepository;
import com.lab.repository.TestTypeRepository;
import com.lab.service.impl.TestServiceImpl;
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

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@ExtendWith(MockitoExtension.class)
public class TestServiceImplTest {

    @Mock
    private TestRepository testRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private TestTypeRepository testTypeRepository;

    @Mock
    private TestMapperImpl testMapper;

    @InjectMocks
    private TestServiceImpl testService;

    private final Order order = new Order(
            1L,
            new Patient(),
            LocalDateTime.now(),
            Status.REGISTERED,
            "Комментарий"
    );

    private final TestType testType = new TestType(
            1L,
            "name",
            "CODE1",
            "Анализ крови",
            new BigDecimal("30.00")
    );

    private final com.lab.entity.Test test = new com.lab.entity.Test(
            1L,
            order,
            testType,
            LocalDateTime.now(),
            "Результат",
            "Референсные значения",
            TestStatus.COMPLETED
    );

    private final TestRequestDTO testRequestDTO = new TestRequestDTO(
            order.getId(),
            testType.getId(),
            "Результат",
            "Референсные значения",
            TestStatus.COMPLETED
    );

    private final TestResponseDTO testResponseDTO = new TestResponseDTO(
            1L,
            1L,
            1L,
            LocalDateTime.now(),
            "Результат",
            "Референсные значения",
            TestStatus.COMPLETED
    );

    @Test
    void getAllTests_ShouldReturnTests_WhenTestsExist() {
        Page<com.lab.entity.Test> page = new PageImpl<>(List.of(test));
        when(testRepository.findAll(any(Pageable.class))).thenReturn(page);
        when(testMapper.toResponseDTOList(anyList())).thenReturn(List.of(testResponseDTO));

        Page<TestResponseDTO> result = testService.getAllTests(Pageable.unpaged());

        assertFalse(result.isEmpty());
        assertEquals(1, result.getTotalElements());
        verify(testRepository).findAll(any(Pageable.class));
    }

    @Test
    void getAllTests_ShouldThrowException_WhenNoTestsFound() {
        when(testRepository.findAll(any(Pageable.class))).thenReturn(Page.empty());

        assertThrows(TestNotFoundException.class, () ->
                testService.getAllTests(Pageable.unpaged()));
    }

    @Test
    void getTestById_ShouldReturnTest_WhenTestExists() {
        when(testRepository.findById(1L)).thenReturn(Optional.of(test));
        when(testMapper.toResponseDTO(any(com.lab.entity.Test.class))).thenReturn(testResponseDTO);

        TestResponseDTO result = testService.getTestById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void getTestById_ShouldThrowException_WhenTestNotFound() {
        when(testRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(TestNotFoundException.class, () ->
                testService.getTestById(1L));
    }

    @Test
    void createTest_ShouldCreateTest_WhenOrderAndTestTypeExist() {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(testTypeRepository.findById(1L)).thenReturn(Optional.of(testType));
        when(testMapper.toEntity(any(TestRequestDTO.class), any(Order.class), any(TestType.class))).thenReturn(test);
        when(testRepository.save(any(com.lab.entity.Test.class))).thenReturn(test);
        when(testMapper.toResponseDTO(any(com.lab.entity.Test.class))).thenReturn(testResponseDTO);

        TestResponseDTO result = testService.createTest(testRequestDTO);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(testRepository).save(any(com.lab.entity.Test.class));
    }

    @Test
    void createTest_ShouldThrowException_WhenOrderNotFound() {
        when(orderRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(OrderNotFoundException.class, () ->
                testService.createTest(testRequestDTO));
    }

    @Test
    void createTest_ShouldThrowException_WhenTestTypeNotFound() {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(testTypeRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(TestTypeNotFoundException.class, () ->
                testService.createTest(testRequestDTO));
    }

    @Test
    void updateTest_ShouldUpdateTest_WhenTestExists() {
        when(testRepository.findById(1L)).thenReturn(Optional.of(test));
        when(testRepository.save(any(com.lab.entity.Test.class))).thenReturn(test);
        when(testMapper.toResponseDTO(any(com.lab.entity.Test.class))).thenReturn(testResponseDTO);

        TestResponseDTO result = testService.updateTest(1L, testRequestDTO);

        assertNotNull(result);
        verify(testRepository).save(test);
    }

    @Test
    void updateTest_ShouldThrowException_WhenTestNotFound() {
        when(testRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(TestNotFoundException.class, () ->
                testService.updateTest(1L, testRequestDTO));
    }

    @Test
    void updateTestResult_ShouldUpdateResult_WhenTestExists() {
        when(testRepository.findById(1L)).thenReturn(Optional.of(test));
        when(testRepository.save(any(com.lab.entity.Test.class))).thenReturn(test);
        when(testMapper.toResponseDTO(any(com.lab.entity.Test.class))).thenReturn(testResponseDTO);

        TestResponseDTO result = testService.updateTestResult(1L, "Новый результат");

        assertNotNull(result);
        verify(testRepository).save(test);
    }

    @Test
    void updateTestResult_ShouldAppendResult_WhenResultExists() {
        com.lab.entity.Test existingTest = new com.lab.entity.Test(
                1L,
                order,
                testType,
                LocalDateTime.now(),
                "Старый результат",
                "Референсные значения",
                TestStatus.COMPLETED
        );

        when(testRepository.findById(1L)).thenReturn(Optional.of(existingTest));
        when(testRepository.save(any(com.lab.entity.Test.class))).thenReturn(existingTest);
        when(testMapper.toResponseDTO(any(com.lab.entity.Test.class))).thenReturn(testResponseDTO);

        testService.updateTestResult(1L, "Новый результат");

        assertEquals("Старый результат, Новый результат", existingTest.getResult());
    }

    @Test
    void updateTestResult_ShouldSetResult_WhenNoResultExists() {
        com.lab.entity.Test emptyResultTest = new com.lab.entity.Test(
                1L,
                order,
                testType,
                LocalDateTime.now(),
                null,
                "Референсные значения",
                TestStatus.COMPLETED
        );

        when(testRepository.findById(1L)).thenReturn(Optional.of(emptyResultTest));
        when(testRepository.save(any(com.lab.entity.Test.class))).thenReturn(emptyResultTest);
        when(testMapper.toResponseDTO(any(com.lab.entity.Test.class))).thenReturn(testResponseDTO);

        testService.updateTestResult(1L, "Новый результат");

        assertEquals("Новый результат", emptyResultTest.getResult());
    }

    @Test
    void updateTestResult_ShouldThrowException_WhenTestNotFound() {
        when(testRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(TestNotFoundException.class, () ->
                testService.updateTestResult(1L, "Новый результат"));
    }
}