package com.lab.mapper;

import com.lab.dto.request.TestRequestDTO;
import com.lab.dto.response.TestResponseDTO;
import com.lab.entity.Order;
import com.lab.entity.Test;
import com.lab.entity.TestType;

import java.util.List;

public interface TestMapper {
    TestResponseDTO toResponseDTO(Test test);
    List<TestResponseDTO> toResponseDTOList(List<Test> tests);
    Test toEntity(TestRequestDTO dto, Order order, TestType testType);
}
