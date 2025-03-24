package com.lab.service;

import com.lab.dto.TestTypeDTO;
import com.lab.entity.TestType;
import com.lab.exception.TestTypeNotFoundException;
import com.lab.repository.TestTypeRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TestTypeService {

    private final TestTypeRepository testTypeRepository;

    public TestTypeService(TestTypeRepository testTypeRepository) {
        this.testTypeRepository = testTypeRepository;
    }

    public List<TestTypeDTO> getAllTestTypes() {
        List<TestType> testTypes = testTypeRepository.findAll();
        if (testTypes.isEmpty()) {
            throw new TestTypeNotFoundException("Типов исследования не найдено");
        }
        return testTypes.stream()
                .map(TestTypeDTO::new)
                .collect(Collectors.toList());
    }

    public TestTypeDTO getTestTypeById(Long id) {
        TestType testType = testTypeRepository.findById(id)
                .orElseThrow(() -> new TestTypeNotFoundException("Типа исследования с id-" + id + " не найдено"));
        return new TestTypeDTO(testType);
    }

    public TestTypeDTO createTestType(TestTypeDTO testTypeDTO) {
        TestType testType = new TestType();
        testType.setName(testTypeDTO.getName());
        testType.setDescription(testTypeDTO.getDescription());
        testType.setPrice(testTypeDTO.getPrice());

        testType = testTypeRepository.save(testType);
        return new TestTypeDTO(testType);
    }

    public TestTypeDTO updateTestType(Long id, TestTypeDTO testTypeDTO) {
        TestType testType = testTypeRepository.findById(id)
                .orElseThrow(() -> new TestTypeNotFoundException("Типа исследования с id-" + id + " не найдено"));

        testType.setName(testTypeDTO.getName());
        testType.setDescription(testTypeDTO.getDescription());
        testType.setCode(testTypeDTO.getCode());
        testType.setPrice(testTypeDTO.getPrice());

        testType = testTypeRepository.save(testType);
        return new TestTypeDTO(testType);
    }

    @Transactional
    public void deleteTestType(Long id) {
        TestType testType = testTypeRepository.findById(id)
                .orElseThrow(() -> new TestTypeNotFoundException("Типа исследования с id-" + id + " не найдено"));
        testTypeRepository.delete(testType);
    }
}
