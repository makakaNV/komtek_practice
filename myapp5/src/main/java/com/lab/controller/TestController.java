package com.lab.controller;

import com.lab.dto.TestDTO;
import com.lab.dto.TestTypeDTO;
import com.lab.exception.ErrorResponse;
import com.lab.service.TestService;
import com.lab.entity.TestStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/tests")
@Tag(name = "Tests", description = "Управление тестами")
public class TestController {

    private final TestService testService;

    public TestController(TestService testService) {
        this.testService = testService;
    }

    @GetMapping
    @Operation(
            summary = "Получить все лаб. исследования",
            description = "Возвращает список всех лаб. исследований"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Список тестов успешно получен",
            content = @Content(schema = @Schema(implementation = TestDTO.class))
    )
    @ApiResponse(
            responseCode = "404",
            description = "Лаб. исследования не найдены",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
    )
    public ResponseEntity<List<TestDTO>> getAllTests() {
        List<TestDTO> tests = testService.getAllTests();
        return ResponseEntity.ok(tests);
    }


    @GetMapping("/{id}")
    @Operation(
            summary = "Получить лаб. исследования по ID",
            description = "Возвращает лаб. исследования по указанному ID"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Лаб. исследования успешно найдены",
            content = @Content(schema = @Schema(implementation = TestDTO.class))
    )
    @ApiResponse(
            responseCode = "404",
            description = "Лаб исследования не найдены",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
    )
    public ResponseEntity<TestDTO> getTestById(@PathVariable Long id) {
        TestDTO testDTO = testService.getTestById(id);
        return ResponseEntity.ok(testDTO);
    }


    @PostMapping
    @Operation(
            summary = "Создать новое лаб. исследование",
            description = "Создает новое лаб. исследование на основе переданных данных"
    )
    @ApiResponse(
            responseCode = "201",
            description = "Лаб. исследование успешно создано",
            content = @Content(schema = @Schema(implementation = TestDTO.class))
    )
    @ApiResponse(
            responseCode = "400",
            description = "Некорректные данные лаб. исследования",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
    )
    public ResponseEntity<TestDTO> createTest(@Valid @RequestBody TestDTO testDTO) {
        TestDTO createdTest = testService.createTest(testDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTest);
    }


    @PutMapping("/{id}")
    @Operation(
            summary = "Обновить лаб. исследование",
            description = "Обновляет данные лаб. исследования по указанному ID"
    )
    @ApiResponse(
            responseCode = "200",
            description = "лаб. исследование успешно обновлено",
            content = @Content(schema = @Schema(implementation = TestDTO.class))
    )
    @ApiResponse(
            responseCode = "400",
            description = "Некорректные данные лаб. исследования",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
    )
    @ApiResponse(
            responseCode = "404",
            description = "лаб. исследование не найдено",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
    )
    public ResponseEntity<TestDTO> updateTest(@PathVariable Long id,@Valid @RequestBody TestDTO testDTO) {
        TestDTO updatedTest = testService.updateTest(id, testDTO);
        return ResponseEntity.ok(updatedTest);
    }


    @PutMapping("/{id}/result")
    @Operation(
            summary = "Добавляет результат лаб. исследования",
            description = "Добавляет результат лаб. исследования по указанному ID"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Результат лаб. исследования успешно добавлен",
            content = @Content(schema = @Schema(implementation = TestDTO.class))
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
    public ResponseEntity<TestDTO> updateTestResult(
            @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(schema = @Schema(example = "{ \"result\": \"New result\" }"))
            )
            @RequestBody Map<String, String> resultUpdate) {
        String newResult = resultUpdate.get("result");
        TestDTO updatedTest = testService.updateTestResult(id, newResult);
        return ResponseEntity.ok(updatedTest);
    }

}
