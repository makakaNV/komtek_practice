package com.lab.controller;


import com.lab.dto.OrderDTO;
import com.lab.dto.TestTypeDTO;
import com.lab.exception.ErrorResponse;
import com.lab.service.TestTypeService;
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

@RestController
@RequestMapping("/api/v1/test-types")
@Tag(name = "TestTypes", description = "Управление типами тестов")
public class TestTypeController {

    private final TestTypeService testTypeService;

    public TestTypeController(TestTypeService testTypeService) {
        this.testTypeService = testTypeService;
    }


    @GetMapping
    @Operation(
            summary = "Получить все типы лаб. исследований",
            description = "Возвращает список всех типов лаб. исследований"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Список типов лаб. исследований успешно получен",
            content = @Content(schema = @Schema(implementation = TestTypeDTO.class))
    )
    @ApiResponse(
            responseCode = "404",
            description = "Типы лаб. исследования не найдены",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
    )
    public ResponseEntity<List<TestTypeDTO>> getAllTestTypes() {
        List<TestTypeDTO> testTypes = testTypeService.getAllTestTypes();
        return ResponseEntity.ok(testTypes);
    }


    @GetMapping("/{id}")
    @Operation(
            summary = "Получить тип лаб. исследований по ID",
            description = "Возвращает тип лаб. исследований по указанному идентификатору"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Тип лаб. исследований успешно найден",
            content = @Content(schema = @Schema(implementation = TestTypeDTO.class))
    )
    @ApiResponse(
            responseCode = "404",
            description = "Тип лаб. исследований не найден",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
    )
    public ResponseEntity<TestTypeDTO> getTestTypeById(@PathVariable Long id) {
        TestTypeDTO testType = testTypeService.getTestTypeById(id);
        return ResponseEntity.ok(testType);
    }


    @PostMapping
    @Operation(
            summary = "Создать новый тип лаб. исследований",
            description = "Создает новый тип лаб. исследований на основе переданных данных"
    )
    @ApiResponse(
            responseCode = "201",
            description = "Тип лаб. исследований успешно создан",
            content = @Content(schema = @Schema(implementation = TestTypeDTO.class))
    )
    @ApiResponse(
            responseCode = "400",
            description = "Некорректные данные типа лаб. исследований",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
    )
    public ResponseEntity<TestTypeDTO> createTestType(@Valid @RequestBody TestTypeDTO testTypeDTO) {
        TestTypeDTO createdTestType = testTypeService.createTestType(testTypeDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTestType);
    }


    @PutMapping("/{id}")
    @Operation(
            summary = "Обновить тип лаб. исследований",
            description = "Обновляет данные типа лаб. исследований по указанному id"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Тип лаб. исследований успешно обновлен",
            content = @Content(schema = @Schema(implementation = TestTypeDTO.class))
    )
    @ApiResponse(
            responseCode = "400",
            description = "Некорректные данные типа лаб. исследований",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
    )
    @ApiResponse(
            responseCode = "404",
            description = "Тип лаб. исследований не найден",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
    )
    public ResponseEntity<TestTypeDTO> updateTestType(@PathVariable Long id, @Valid @RequestBody TestTypeDTO testTypeDTO) {
        TestTypeDTO updatedTestType = testTypeService.updateTestType(id, testTypeDTO);
        return ResponseEntity.ok(updatedTestType);
    }


    @DeleteMapping("/{id}")
    @Operation(
            summary = "Удалить тип лаб. исследований",
            description = "Удаляет тип лаб. исследований по указанному идентификатору"
    )
    @ApiResponse(
            responseCode = "204",
            description = "Тип лаб. исследований успешно удален"
    )
    @ApiResponse(
            responseCode = "404",
            description = "Тип лаб. исследований не найден",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
    )
    public ResponseEntity<Void> deleteTestType(@PathVariable Long id) {
        testTypeService.deleteTestType(id);
        return ResponseEntity.noContent().build();
    }
}