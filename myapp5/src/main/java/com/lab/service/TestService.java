package com.lab.service;

import com.lab.dto.TestDTO;
import com.lab.entity.Order;
import com.lab.entity.Test;
import com.lab.entity.TestType;
import com.lab.exception.OrderNotFoundException;
import com.lab.exception.TestNotFoundException;
import com.lab.exception.TestTypeNotFoundException;
import com.lab.repository.OrderRepository;
import com.lab.repository.TestRepository;
import com.lab.repository.TestTypeRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TestService {

    private final TestRepository testRepository;
    private final OrderRepository orderRepository;
    private final TestTypeRepository testTypeRepository;

    public TestService(TestRepository testRepository, OrderRepository orderRepository, TestTypeRepository testTypeRepository) {
        this.testRepository = testRepository;
        this.orderRepository = orderRepository;
        this.testTypeRepository = testTypeRepository;
    }

    public List<TestDTO> getAllTests() {
        return testRepository.findAll().stream()
                .map(TestDTO::new)
                .collect(Collectors.toList());
    }

    public TestDTO getTestById(Long id) {
        Test test = testRepository.findById(id)
                .orElseThrow(() -> new TestNotFoundException("Тест с id-" + id + " не найден"));
        return new TestDTO(test);
    }

    public TestDTO createTest(TestDTO testDTO) {
        Order order = orderRepository.findById(testDTO.getOrderId())
                .orElseThrow(() -> new OrderNotFoundException("Заявки с id- " + testDTO.getOrderId() + " не найдено"));

        TestType testType = testTypeRepository.findById(testDTO.getTestTypeId())
                .orElseThrow(() -> new TestTypeNotFoundException("Тип исследования с id- " + testDTO.getTestTypeId() + " не найден"));

        Test test = new Test();
        test.setOrder(order);
        test.setTestType(testType);
        test.setExecutionDate(LocalDateTime.now());
        test.setResult(testDTO.getResult());
        test.setReferenceValues(testDTO.getReferenceValues());
        test.setStatus(testDTO.getStatus());

        test = testRepository.save(test);
        return new TestDTO(test);
    }

    public TestDTO updateTest(Long id, TestDTO testDTO) {
        Test test = testRepository.findById(id)
                .orElseThrow(() -> new TestNotFoundException("Тест с id-" + id + " не найден"));

        test.setExecutionDate(LocalDateTime.now());
        test.setResult(testDTO.getResult());
        test.setReferenceValues(testDTO.getReferenceValues());
        test.setStatus(testDTO.getStatus());

        test = testRepository.save(test);
        return new TestDTO(test);
    }

    public TestDTO updateTestResult(Long id, String newResult) {
        Test test = testRepository.findById(id)
                .orElseThrow(() -> new TestNotFoundException("Тест с id-" + id + " не найден"));

        if (test.getResult() != null && !test.getResult().isEmpty()) {
            test.setResult(test.getResult() + ", " + newResult);
        } else {
            test.setResult(newResult);
        }

        test = testRepository.save(test);
        return new TestDTO(test);
    }
}