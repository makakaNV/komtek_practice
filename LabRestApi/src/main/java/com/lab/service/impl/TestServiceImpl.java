package com.lab.service.impl;

import com.lab.dto.request.TestRequestDTO;
import com.lab.dto.response.TestResponseDTO;
import com.lab.entity.Order;
import com.lab.entity.Test;
import com.lab.entity.TestType;
import com.lab.exception.OrderNotFoundException;
import com.lab.exception.TestNotFoundException;
import com.lab.exception.TestTypeNotFoundException;
import com.lab.mapper.impl.TestMapperImpl;
import com.lab.repository.OrderRepository;
import com.lab.repository.TestRepository;
import com.lab.repository.TestTypeRepository;
import com.lab.service.TestService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class TestServiceImpl implements TestService {

    private final TestRepository testRepository;
    private final OrderRepository orderRepository;
    private final TestTypeRepository testTypeRepository;
    private final TestMapperImpl testMapperImpl;


    public TestServiceImpl(
            TestRepository testRepository,
            OrderRepository orderRepository,
            TestTypeRepository testTypeRepository,
            TestMapperImpl testMapperImpl
    ) {
        this.testRepository = testRepository;
        this.orderRepository = orderRepository;
        this.testTypeRepository = testTypeRepository;
        this.testMapperImpl = testMapperImpl;
    }

    @Override
    public Page<TestResponseDTO> getAllTests(Pageable pageable) {
        Page<Test> testsPage = testRepository.findAll(pageable);

        if (testsPage.isEmpty()) {
            throw new TestNotFoundException("Лаб. исследований не найдено");
        }

        return testsPage.map(testMapperImpl::toResponseDTO);
    }

    @Override
    public TestResponseDTO getTestById(Long id) {
        Test test = testRepository.findById(id)
                .orElseThrow(() -> new TestNotFoundException("Тест с id-" + id + " не найден"));
        return testMapperImpl.toResponseDTO(test);
    }

    @Override
    public TestResponseDTO createTest(TestRequestDTO testDTO) {
        Order order = orderRepository.findById(testDTO.getOrderId())
                .orElseThrow(() -> new OrderNotFoundException("Заявки с id- "
                        + testDTO.getOrderId() + " не найдено"));

        TestType testType = testTypeRepository.findById(testDTO.getTestTypeId())
                .orElseThrow(() -> new TestTypeNotFoundException("Тип исследования с id- "
                        + testDTO.getTestTypeId() + " не найден"));

        Test test = testMapperImpl.toEntity(testDTO, order, testType);

        test = testRepository.save(test);

        return testMapperImpl.toResponseDTO(test);
    }

    @Override
    public TestResponseDTO updateTest(Long id, TestRequestDTO testDTO) {
        Test test = testRepository.findById(id)
                .orElseThrow(() -> new TestNotFoundException("Тест с id-" + id + " не найден"));

        test.setExecutionDate(LocalDateTime.now());
        test.setResult(testDTO.getResult());
        test.setReferenceValues(testDTO.getReferenceValues());
        test.setStatus(testDTO.getStatus());

        test = testRepository.save(test);
        return testMapperImpl.toResponseDTO(test);
    }

    @Override
    public TestResponseDTO updateTestResult(Long id, String newResult) {
        Test test = testRepository.findById(id)
                .orElseThrow(() -> new TestNotFoundException("Тест с id-" + id + " не найден"));

        if (test.getResult() != null && !test.getResult().isEmpty()) {
            test.setResult(test.getResult() + ", " + newResult);
        } else {
            test.setResult(newResult);
        }

        test = testRepository.save(test);
        return testMapperImpl.toResponseDTO(test);
    }
}