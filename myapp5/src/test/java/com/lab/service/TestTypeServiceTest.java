package com.lab.service;

import com.lab.dto.TestTypeDTO;
import com.lab.entity.TestType;
import com.lab.exception.TestTypeNotFoundException;
import com.lab.repository.TestTypeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


public class TestTypeServiceTest {

    @Mock
    private TestTypeRepository testTypeRepository;

    @InjectMocks
    private TestTypeService testTypeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllTestTypes_WhenTestTypesExist_ShouldReturnListOfTestTypes () {
        TestType testType = new TestType(1L, "Анализ крови", "code", "desciption", new BigDecimal("150.00"));
        when(testTypeRepository.findAll()).thenReturn(List.of(testType));

        List<TestTypeDTO> result = testTypeService.getAllTestTypes();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Анализ крови", result.get(0).getName());
        verify(testTypeRepository, times(1)).findAll();
    }

    @Test
    void getAllTestTypes_WhenTestTypesDoNotExist_ShouldThrowTestTypeNotFoundException() {
        when(testTypeRepository.findAll()).thenReturn(Collections.emptyList());

        assertThrows(TestTypeNotFoundException.class, () -> testTypeService.getAllTestTypes());
        verify(testTypeRepository, times(1)).findAll();
    }

    @Test
    void getTestTypeById_WhenTestTypeExists_ShouldReturnTestTypeDTO() {
        Long testTypeId = 1L;
        TestType testType = new TestType(testTypeId, "Анализ крови", "code", "desciption", new BigDecimal("150.00"));
        when(testTypeRepository.findById(testTypeId)).thenReturn(Optional.of(testType));

        TestTypeDTO result = testTypeService.getTestTypeById(testTypeId);

        assertNotNull(result);
        assertEquals(testTypeId, result.getId());
        assertEquals("Анализ крови", result.getName());
        assertEquals("code", result.getCode());
        verify(testTypeRepository, times(1)).findById(testTypeId);
    }

    @Test
    void getTestTypeById_WhenTestTypeDoesNotExist_ShouldThrowTestTypeNotFoundException() {
        Long testTypeId = 999L;
        when(testTypeRepository.findById(testTypeId)).thenReturn(Optional.empty());

        assertThrows(TestTypeNotFoundException.class, () -> testTypeService.getTestTypeById(testTypeId));
        verify(testTypeRepository, times(1)).findById(testTypeId);
    }

    @Test
    void createTestType_ShouldReturnCreatedTestTypeDTO() {
        TestTypeDTO testTypeDTO = new TestTypeDTO(null, "Анализ крови", "AN-001", "Описание анализа крови", new BigDecimal("100.00"));
        TestType savedTestType = new TestType(1L, "Анализ крови", "AN-001", "Описание анализа крови", new BigDecimal("100.00"));

        when(testTypeRepository.save(any(TestType.class))).thenReturn(savedTestType);

        TestTypeDTO result = testTypeService.createTestType(testTypeDTO);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Анализ крови", result.getName());
        verify(testTypeRepository, times(1)).save(any(TestType.class));
    }

    @Test
    void updateTestType_WhenTestTypeExists_ShouldReturnUpdatedTestTypeDTO() {
        Long testTypeId = 1L;
        TestType existingTestType = new TestType(testTypeId, "Анализ крови", "code1", "description1", new BigDecimal("100.00"));
        TestTypeDTO updatedTestTypeDTO = new TestTypeDTO(testTypeId, "Обновленный анализ крови", "code2", "description2", new BigDecimal("150.00"));
        when(testTypeRepository.findById(testTypeId)).thenReturn(Optional.of(existingTestType));
        when(testTypeRepository.save(any(TestType.class))).thenReturn(existingTestType);

        TestTypeDTO result = testTypeService.updateTestType(testTypeId, updatedTestTypeDTO);


        assertNotNull(result);
        assertEquals("Обновленный анализ крови", result.getName());
        assertEquals("code2", result.getCode());
        verify(testTypeRepository, times(1)).findById(testTypeId);
        verify(testTypeRepository, times(1)).save(any(TestType.class));
    }

    @Test
    void updateTestType_WhenTestTypeDoesNotExist_ShouldThrowTestTypeNotFoundException() {
        Long testTypeId = 999L;
        TestTypeDTO updatedTestTypeDTO = new TestTypeDTO(testTypeId, "Обновленный анализ крови", "code", "description", new BigDecimal("150.00"));
        when(testTypeRepository.findById(testTypeId)).thenReturn(Optional.empty());

        assertThrows(TestTypeNotFoundException.class, () -> testTypeService.updateTestType(testTypeId, updatedTestTypeDTO));
        verify(testTypeRepository, times(1)).findById(testTypeId);
    }

    @Test
    void deleteTestType_WhenTestTypeExists_ShouldDeleteTestType() {
        Long testTypeId = 1L;
        TestType testType = new TestType(testTypeId, "Анализ крови", "code", "description", new BigDecimal("150.00"));
        when(testTypeRepository.findById(testTypeId)).thenReturn(Optional.of(testType));

        testTypeService.deleteTestType(testTypeId);

        verify(testTypeRepository, times(1)).delete(testType);
    }

    @Test
    void deleteTestType_WhenTestTypeDoesNotExist_ShouldThrowTestTypeNotFoundException() {
        Long testTypeId = 999L;
        when(testTypeRepository.findById(testTypeId)).thenReturn(Optional.empty());

        assertThrows(TestTypeNotFoundException.class, () -> testTypeService.deleteTestType(testTypeId));
        verify(testTypeRepository, times(1)).findById(testTypeId);
    }
}
