package com.lab.service;

import com.lab.dto.request.TestRequestDTO;
import com.lab.dto.response.TestResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TestService {
    Page<TestResponseDTO> getAllTests(Pageable pageable);
    TestResponseDTO getTestById(Long id);
    TestResponseDTO createTest(TestRequestDTO testDTO);
    TestResponseDTO updateTest(Long id, TestRequestDTO testDTO);
    TestResponseDTO updateTestResult(Long id, String newResult);

    byte[] generateTestPdf(Long id);
}
