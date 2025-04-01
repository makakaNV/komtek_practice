package com.lab.mapper.impl;

import com.lab.dto.request.TestRequestDTO;
import com.lab.dto.response.TestResponseDTO;
import com.lab.entity.Order;
import com.lab.entity.Test;
import com.lab.entity.TestType;
import com.lab.mapper.TestMapper;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class TestMapperImpl implements TestMapper {

    public TestResponseDTO toResponseDTO(Test test) {
        return TestResponseDTO.builder()
                .id(test.getId())
                .orderId(test.getOrder().getId())
                .testTypeId(test.getTestType().getId())
                .executionDate(test.getExecutionDate())
                .result(test.getResult())
                .referenceValues(test.getReferenceValues())
                .status(test.getStatus())
                .build();
    }

    public List<TestResponseDTO> toResponseDTOList(List<Test> tests) {
        return tests.stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    public Test toEntity(TestRequestDTO dto, Order order, TestType testType) {
        return Test.builder()
                .order(order)
                .testType(testType)
                .executionDate(LocalDateTime.now())
                .result(dto.getResult())
                .referenceValues(dto.getReferenceValues())
                .status(dto.getStatus())
                .build();
    }
}
