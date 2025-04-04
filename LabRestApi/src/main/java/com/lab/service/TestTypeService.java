package com.lab.service;

import com.lab.dto.request.TestTypeRequestDTO;
import com.lab.dto.response.TestTypeResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TestTypeService {
    Page<TestTypeResponseDTO> getAllTestTypes(Pageable pageable);
    TestTypeResponseDTO getTestTypeById(Long id);
    TestTypeResponseDTO createTestType(TestTypeRequestDTO testTypeDTO);
    TestTypeResponseDTO updateTestType(Long id, TestTypeRequestDTO testTypeDTO);
    void deleteTestType(Long id);
}
