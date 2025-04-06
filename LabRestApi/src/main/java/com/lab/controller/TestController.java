package com.lab.controller;

import com.lab.dto.request.TestRequestDTO;
import com.lab.dto.response.TestResponseDTO;
import com.lab.exception.ErrorResponse;
import com.lab.service.impl.TestServiceImpl;
import com.lab.util.PaginationUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/tests")
@Tag(name = "Tests", description = "Управление тестами")
@SuppressWarnings("unused")
public class TestController {

    private final TestServiceImpl testServiceImpl;

    public TestController(TestServiceImpl testServiceImpl) {
        this.testServiceImpl = testServiceImpl;
    }

    @GetMapping
    @Operation(
            summary = "Получить все лаб. исследования",
            description = "Возвращает список всех лаб. исследований. Реализована пагинация",
            security = { @SecurityRequirement(name = "bearerAuth") }
    )
    @ApiResponse(
            responseCode = "200",
            description = "Список тестов успешно получен",
            content = @Content(schema = @Schema(implementation = TestResponseDTO.class))
    )
    @ApiResponse(
            responseCode = "404",
            description = "Лаб. исследования не найдены",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
    )
    @ApiResponse(
            responseCode = "401",
            description = "Требуется авторизация. Необходим токен.",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
    )
    public ResponseEntity<List<TestResponseDTO>> getAllTests(
            @ParameterObject
            @PageableDefault(size = 50)
            Pageable pageable
    ) {
        Page<TestResponseDTO> tests = testServiceImpl.getAllTests(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(tests, "/api/v1/tests");
        return new ResponseEntity<>(tests.getContent(), headers, HttpStatus.OK);
    }


    @GetMapping("/{id}")
    @Operation(
            summary = "Получить лаб. исследования по ID",
            description = "Возвращает лаб. исследования по указанному ID",
            security = { @SecurityRequirement(name = "bearerAuth") }
    )
    @ApiResponse(
            responseCode = "200",
            description = "Лаб. исследования успешно найдены",
            content = @Content(schema = @Schema(implementation = TestResponseDTO.class))
    )
    @ApiResponse(
            responseCode = "404",
            description = "Лаб исследования не найдены",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
    )
    public ResponseEntity<TestResponseDTO> getTestById(@PathVariable Long id) {
        TestResponseDTO testDTO = testServiceImpl.getTestById(id);
        return ResponseEntity.ok(testDTO);
    }


    @PostMapping
    @Operation(
            summary = "Создать новое лаб. исследование",
            description = "Создает новое лаб. исследование на основе переданных данных",
            security = { @SecurityRequirement(name = "bearerAuth") }
    )
    @ApiResponse(
            responseCode = "201",
            description = "Лаб. исследование успешно создано",
            content = @Content(schema = @Schema(implementation = TestResponseDTO.class))
    )
    @ApiResponse(
            responseCode = "400",
            description = "Некорректные данные лаб. исследования",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
    )
    public ResponseEntity<TestResponseDTO> createTest(@Valid @RequestBody TestRequestDTO testDTO) {
        TestResponseDTO createdTest = testServiceImpl.createTest(testDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTest);
    }


    @PutMapping("/{id}")
    @Operation(
            summary = "Обновить лаб. исследование",
            description = "Обновляет данные лаб. исследования по указанному ID",
            security = { @SecurityRequirement(name = "bearerAuth") }
    )
    @ApiResponse(
            responseCode = "200",
            description = "лаб. исследование успешно обновлено",
            content = @Content(schema = @Schema(implementation = TestResponseDTO.class))
    )
    @ApiResponse(
            responseCode = "400",
            description = "Некорректные данные лаб. исследования",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
    )
    @ApiResponse(
            responseCode = "404",
            description = "Лаб. исследование не найдено",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
    )
    public ResponseEntity<TestResponseDTO> updateTest(
            @PathVariable Long id,
            @Valid @RequestBody TestRequestDTO testDTO
    ) {
        TestResponseDTO updatedTest = testServiceImpl.updateTest(id, testDTO);
        return ResponseEntity.ok(updatedTest);
    }


    @PutMapping("/{id}/result")
    @Operation(
            summary = "Добавляет результат лаб. исследования",
            description = "Добавляет результат лаб. исследования по указанному ID",
            security = { @SecurityRequirement(name = "bearerAuth") }
    )
    @ApiResponse(
            responseCode = "200",
            description = "Результат лаб. исследования успешно добавлен",
            content = @Content(schema = @Schema(implementation = TestResponseDTO.class))
    )
    @ApiResponse(
            responseCode = "400",
            description = "Некорректные данные для обновления лаб. исследования",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
    )
    @ApiResponse(
            responseCode = "404",
            description = "Лаб. исследование не найдено",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
    )
    public ResponseEntity<TestResponseDTO> updateTestResult(
            @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(schema = @Schema(example = "{ \"result\": \"New result\" }"))
            )
            @RequestBody Map<String, String> resultUpdate) {
        String newResult = resultUpdate.get("result");
        TestResponseDTO updatedTest = testServiceImpl.updateTestResult(id, newResult);
        return ResponseEntity.ok(updatedTest);
    }

    @GetMapping("/{id}/pdf")
    @Operation(
            summary = "Выгружает PDF файл лаб. исследования",
            description = "Генерирует и загружает PDF файл с данными лаб. исследования по ID",
            security = { @SecurityRequirement(name = "bearerAuth") }
    )
    @ApiResponse(
            responseCode = "200",
            description = "PDF файл успешно сгенерирован",
            content = @Content(mediaType = "application/pdf")
    )
    @ApiResponse(
            responseCode = "404",
            description = "Лаб. исследование не найдено",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
    )
    @ApiResponse(
            responseCode = "500",
            description = "Ошибка генерации PDF",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
    )
    public ResponseEntity<byte[]> generatePdf(@PathVariable Long id) {
        byte[] pdfBytes = testServiceImpl.generateTestPdf(id);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDisposition(ContentDisposition.builder("attachment")
                .filename("test_" + id + ".pdf")
                .build());

        return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
    }

}
