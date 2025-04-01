package com.lab.service;

import com.lab.dto.request.TestTypeRequestDTO;
import com.lab.dto.response.TestTypeResponseDTO;
import com.lab.entity.*;
import com.lab.exception.TestTypeNotFoundException;
import com.lab.mapper.impl.TestTypeMapperImpl;
import com.lab.repository.TestTypeRepository;
import com.lab.service.impl.TestTypeServiceImpl;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;


@ExtendWith(MockitoExtension.class)
public class TestTypeServiceImplTest {

    @Mock
    private TestTypeRepository testTypeRepository;

    @Mock
    private TestTypeMapperImpl testTypeMapper;

    @InjectMocks
    private TestTypeServiceImpl testTypeService;

    private final TestType testType = new TestType(
            1L,
            "HIV",
            "CODE_HIV",
            "HIV_TEST123",
            new BigDecimal("30.00")
    );

    private final TestTypeRequestDTO testTypeRequestDTO = new TestTypeRequestDTO(
            "HIV",
            "CODE_HIV",
            "HIV_TEST123",
            new BigDecimal("30.00")
    );

    private final TestTypeResponseDTO testTypeResponseDTO = new TestTypeResponseDTO(
            1L,
            "HIV",
            "CODE_HIV",
            "HIV_TEST123",
            new BigDecimal("30.00")
    );

    @Test
    void getAllTestTypes_ShouldReturnTestTypes_WhenTestTypesExist() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<TestType> testTypesPage = mock(Page.class);
        when(testTypeRepository.findAll(pageable)).thenReturn(testTypesPage);
        when(testTypesPage.isEmpty()).thenReturn(false);
        when(testTypesPage.getContent()).thenReturn(List.of(testType));
        when(testTypeMapper.toResponseDTOList(anyList())).thenReturn(List.of(testTypeResponseDTO));

        Page<TestTypeResponseDTO> result = testTypeService.getAllTestTypes(pageable);

        assertFalse(result.isEmpty());
        assertEquals(1, result.getTotalElements());
        verify(testTypeRepository).findAll(pageable);
    }

    @Test
    void getAllTestTypes_ShouldThrowException_WhenNoTestTypesFound() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<TestType> testTypesPage = mock(Page.class);
        when(testTypeRepository.findAll(pageable)).thenReturn(testTypesPage);
        when(testTypesPage.isEmpty()).thenReturn(true);

        assertThrows(TestTypeNotFoundException.class, () ->
                testTypeService.getAllTestTypes(pageable));
    }

    @Test
    void getTestTypeById_ShouldReturnTestType_WhenTestTypeExists() {
        when(testTypeRepository.findById(1L)).thenReturn(Optional.of(testType));
        when(testTypeMapper.toResponseDTO(any(TestType.class))).thenReturn(testTypeResponseDTO);

        TestTypeResponseDTO result = testTypeService.getTestTypeById(1L);

        assertNotNull(result);
        assertEquals("HIV", result.getName());
        verify(testTypeRepository).findById(1L);
    }

    @Test
    void getTestTypeById_ShouldThrowException_WhenTestTypeNotFound() {
        when(testTypeRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(TestTypeNotFoundException.class, () ->
                testTypeService.getTestTypeById(1L));
    }

    @Test
    void createTestType_ShouldCreateTestType_WhenValidData() {
        when(testTypeMapper.toEntity(any(TestTypeRequestDTO.class))).thenReturn(testType);
        when(testTypeRepository.save(any(TestType.class))).thenReturn(testType);
        when(testTypeMapper.toResponseDTO(any(TestType.class))).thenReturn(testTypeResponseDTO);

        TestTypeResponseDTO result = testTypeService.createTestType(testTypeRequestDTO);

        assertNotNull(result);
        assertEquals("CODE_HIV", result.getCode());
        verify(testTypeRepository).save(any(TestType.class));
    }

    @Test
    void updateTestType_ShouldUpdateTestType_WhenTestTypeExists() {
        when(testTypeRepository.findById(1L)).thenReturn(Optional.of(testType));
        when(testTypeRepository.save(any(TestType.class))).thenReturn(testType);
        when(testTypeMapper.toResponseDTO(any(TestType.class))).thenReturn(testTypeResponseDTO);

        TestTypeResponseDTO result = testTypeService.updateTestType(1L, testTypeRequestDTO);

        assertNotNull(result);
        verify(testTypeRepository).save(testType);
    }

    @Test
    void updateTestType_ShouldThrowException_WhenTestTypeNotFound() {
        when(testTypeRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(TestTypeNotFoundException.class, () ->
                testTypeService.updateTestType(1L, testTypeRequestDTO));
    }

    @Test
    void deleteTestType_ShouldDeleteTestType_WhenTestTypeExists() {
        when(testTypeRepository.findById(1L)).thenReturn(Optional.of(testType));

        testTypeService.deleteTestType(1L);

        verify(testTypeRepository).delete(testType);
    }

    @Test
    void deleteTestType_ShouldThrowException_WhenTestTypeNotFound() {
        when(testTypeRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(TestTypeNotFoundException.class, () ->
                testTypeService.deleteTestType(1L));
    }
}