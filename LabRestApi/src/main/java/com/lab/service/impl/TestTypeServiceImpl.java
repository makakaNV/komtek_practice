package com.lab.service.impl;

import com.lab.cache.impl.CacheServiceImpl;
import com.lab.dto.request.TestTypeRequestDTO;
import com.lab.dto.response.TestTypeResponseDTO;
import com.lab.entity.Test;
import com.lab.entity.TestType;
import com.lab.exception.TestTypeNotFoundException;
import com.lab.mapper.impl.TestTypeMapperImpl;
import com.lab.repository.TestRepository;
import com.lab.repository.TestTypeRepository;
import com.lab.service.TestTypeService;
import jakarta.transaction.Transactional;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class TestTypeServiceImpl implements TestTypeService {

    private final TestTypeRepository testTypeRepository;
    private final TestTypeMapperImpl testTypeMapperImpl;
    private final CacheServiceImpl cacheServiceImpl;
    private final TestRepository testRepository;

    public TestTypeServiceImpl(
            TestTypeRepository testTypeRepository,
            TestTypeMapperImpl testTypeMapperImpl,
            CacheServiceImpl cacheServiceImpl,
            TestRepository testRepository
    ) {
        this.testTypeRepository = testTypeRepository;
        this.testTypeMapperImpl = testTypeMapperImpl;
        this.cacheServiceImpl = cacheServiceImpl;
        this.testRepository = testRepository;
    }

    @Override
    public Page<TestTypeResponseDTO> getAllTestTypes(Pageable pageable) {
        Page<TestType> testTypesPage = testTypeRepository.findAll(pageable);

        if (testTypesPage.isEmpty()) {
            throw new TestTypeNotFoundException("Типов исследования не найдено");
        }
        return testTypesPage.map(testTypeMapperImpl::toResponseDTO);
    }

    @Override
    @Cacheable(value = "testTypes", key = "#id")
    public TestTypeResponseDTO getTestTypeById(Long id) {
        TestType testType = testTypeRepository.findById(id)
                .orElseThrow(() -> new TestTypeNotFoundException("Типа исследования с id-" + id + " не найдено"));
        return testTypeMapperImpl.toResponseDTO(testType);
    }

    @Override
    public TestTypeResponseDTO createTestType(TestTypeRequestDTO testTypeDTO) {
        TestType testType = testTypeMapperImpl.toEntity(testTypeDTO);

        testType = testTypeRepository.save(testType);
        return testTypeMapperImpl.toResponseDTO(testType);
    }

    @Override
    @CachePut(value = "testTypes", key = "#id")
    public TestTypeResponseDTO updateTestType(Long id, TestTypeRequestDTO testTypeDTO) {
        TestType testType = testTypeRepository.findById(id)
                .orElseThrow(() -> new TestTypeNotFoundException("Типа исследования с id-" + id + " не найдено"));

        cacheServiceImpl.evictTestTypeCaches(testType);

        testType.setName(testTypeDTO.getName());
        testType.setDescription(testTypeDTO.getDescription());
        testType.setCode(testTypeDTO.getCode());
        testType.setPrice(testTypeDTO.getPrice());

        testType = testTypeRepository.save(testType);
        return testTypeMapperImpl.toResponseDTO(testType);
    }

    @Override
    @Transactional
    @CacheEvict(value = "testTypes", key = "#id")
    public void deleteTestType(Long id) {
        TestType testType = testTypeRepository.findById(id)
                .orElseThrow(() -> new TestTypeNotFoundException("Типа исследования с id-" + id + " не найдено"));

        List<Test> tests = testRepository.findAllByTestTypeId(testType.getId());
        testRepository.deleteAll(tests);
        tests.forEach(cacheServiceImpl::evictTestCaches);

        cacheServiceImpl.evictTestTypeCaches(testType);

        testTypeRepository.delete(testType);
    }

}
