package com.lab.mapper;

import com.lab.dto.request.TestTypeRequestDTO;
import com.lab.dto.response.TestTypeResponseDTO;
import com.lab.entity.TestType;

import java.util.List;

public interface TestTypeMapper {
    TestTypeResponseDTO toResponseDTO(TestType testType);
    List<TestTypeResponseDTO> toResponseDTOList(List<TestType> testTypes);
    TestType toEntity(TestTypeRequestDTO dto);
}
