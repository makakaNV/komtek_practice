package com.lab.mapper.impl;

import com.lab.dto.request.TestTypeRequestDTO;
import com.lab.dto.response.TestTypeResponseDTO;
import com.lab.entity.TestType;
import com.lab.mapper.TestTypeMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class TestTypeMapperImpl implements TestTypeMapper {

    @Override
    public TestTypeResponseDTO toResponseDTO(TestType testType) {
        return TestTypeResponseDTO.builder()
                .id(testType.getId())
                .name(testType.getName())
                .code(testType.getCode())
                .description(testType.getDescription())
                .price(testType.getPrice())
                .build();
    }

    @Override
    public List<TestTypeResponseDTO> toResponseDTOList(List<TestType> testTypes) {
        return testTypes.stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public TestType toEntity(TestTypeRequestDTO dto) {
        return TestType.builder()
                .name(dto.getName())
                .code(dto.getCode())
                .description(dto.getDescription())
                .price(dto.getPrice())
                .build();
    }
}
