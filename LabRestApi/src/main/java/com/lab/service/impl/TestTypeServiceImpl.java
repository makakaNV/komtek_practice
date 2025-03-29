package com.lab.service.impl;

import com.lab.dto.request.TestTypeRequestDTO;
import com.lab.dto.response.TestTypeResponseDTO;
import com.lab.entity.TestType;
import com.lab.exception.TestTypeNotFoundException;
import com.lab.mapper.TestTypeMapper;
import com.lab.repository.TestTypeRepository;
import com.lab.service.TestTypeService;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TestTypeServiceImpl implements TestTypeService {

    private final TestTypeRepository testTypeRepository;
    private final TestTypeMapper testTypeMapper;

    public TestTypeServiceImpl(
            TestTypeRepository testTypeRepository,
            TestTypeMapper testTypeMapper
    ) {
        this.testTypeRepository = testTypeRepository;
        this.testTypeMapper = testTypeMapper;
    }

    @Override
    public List<TestTypeResponseDTO> getAllTestTypes(Pageable pageable) {
        Page<TestType> testTypesPage = testTypeRepository.findAll(pageable);

        if (testTypesPage.isEmpty()) {
            throw new TestTypeNotFoundException("Типов исследования не найдено");
        }
        return testTypeMapper.toResponseDTOList(testTypesPage.getContent());
    }

    @Override
    public TestTypeResponseDTO getTestTypeById(Long id) {
        TestType testType = testTypeRepository.findById(id)
                .orElseThrow(() -> new TestTypeNotFoundException("Типа исследования с id-" + id + " не найдено"));
        return testTypeMapper.toResponseDTO(testType);
    }

    @Override
    public TestTypeResponseDTO createTestType(TestTypeRequestDTO testTypeDTO) {
        TestType testType = testTypeMapper.toEntity(testTypeDTO);

        testType = testTypeRepository.save(testType);
        return testTypeMapper.toResponseDTO(testType);
    }

    @Override
    public TestTypeResponseDTO updateTestType(Long id, TestTypeRequestDTO testTypeDTO) {
        TestType testType = testTypeRepository.findById(id)
                .orElseThrow(() -> new TestTypeNotFoundException("Типа исследования с id-" + id + " не найдено"));

        testType.setName(testTypeDTO.getName());
        testType.setDescription(testTypeDTO.getDescription());
        testType.setCode(testTypeDTO.getCode());
        testType.setPrice(testTypeDTO.getPrice());

        testType = testTypeRepository.save(testType);
        return testTypeMapper.toResponseDTO(testType);
    }

    @Override
    @Transactional
    public void deleteTestType(Long id) {
        TestType testType = testTypeRepository.findById(id)
                .orElseThrow(() -> new TestTypeNotFoundException("Типа исследования с id-" + id + " не найдено"));
        testTypeRepository.delete(testType);
    }
}
