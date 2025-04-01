package com.lab.controller;

import com.lab.dto.request.TestTypeRequestDTO;
import com.lab.dto.response.TestTypeResponseDTO;
import com.lab.exception.ErrorResponse;
import com.lab.service.impl.TestTypeServiceImpl;
import com.lab.util.PaginationUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/test-types")
@Tag(name = "TestTypes", description = "Управление типами тестов")
public class TestTypeController {

    private final TestTypeServiceImpl testTypeServiceImpl;

    public TestTypeController(TestTypeServiceImpl testTypeServiceImpl) {
        this.testTypeServiceImpl = testTypeServiceImpl;
    }


    @GetMapping
    @SuppressWarnings("unused")
    @Operation(
            summary = "Получить все типы лаб. исследований",
            description = "Возвращает список всех типов лаб. исследований." +
                    " Реализована пагинация: по умолчанию page=0 & size=50"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Список типов лаб. исследований успешно получен",
            content = @Content(schema = @Schema(implementation = TestTypeResponseDTO.class))
    )
    @ApiResponse(
            responseCode = "404",
            description = "Типы лаб. исследования не найдены",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
    )
    public ResponseEntity<List<TestTypeResponseDTO>> getAllTestTypes(
            @ParameterObject
            @PageableDefault(size = 50)
            Pageable pageable
    ) {
        Page<TestTypeResponseDTO> testTypes = testTypeServiceImpl.getAllTestTypes(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(testTypes, "/api/v1/test-types");
        return new ResponseEntity<>(testTypes.getContent(), headers, HttpStatus.OK);
    }


    @GetMapping("/{id}")
    @SuppressWarnings("unused")
    @Operation(
            summary = "Получить тип лаб. исследований по ID",
            description = "Возвращает тип лаб. исследований по указанному идентификатору"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Тип лаб. исследований успешно найден",
            content = @Content(schema = @Schema(implementation = TestTypeResponseDTO.class))
    )
    @ApiResponse(
            responseCode = "404",
            description = "Тип лаб. исследований не найден",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
    )
    public ResponseEntity<TestTypeResponseDTO> getTestTypeById(@PathVariable Long id) {
        TestTypeResponseDTO testType = testTypeServiceImpl.getTestTypeById(id);
        return ResponseEntity.ok(testType);
    }


    @PostMapping
    @SuppressWarnings("unused")
    @Operation(
            summary = "Создать новый тип лаб. исследований",
            description = "Создает новый тип лаб. исследований на основе переданных данных"
    )
    @ApiResponse(
            responseCode = "201",
            description = "Тип лаб. исследований успешно создан",
            content = @Content(schema = @Schema(implementation = TestTypeResponseDTO.class))
    )
    @ApiResponse(
            responseCode = "400",
            description = "Некорректные данные типа лаб. исследований",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
    )
    public ResponseEntity<TestTypeResponseDTO> createTestType(@Valid @RequestBody TestTypeRequestDTO testTypeDTO) {
        TestTypeResponseDTO createdTestType = testTypeServiceImpl.createTestType(testTypeDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTestType);
    }


    @PutMapping("/{id}")
    @SuppressWarnings("unused")
    @Operation(
            summary = "Обновить тип лаб. исследований",
            description = "Обновляет данные типа лаб. исследований по указанному id"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Тип лаб. исследований успешно обновлен",
            content = @Content(schema = @Schema(implementation = TestTypeResponseDTO.class))
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
    public ResponseEntity<TestTypeResponseDTO> updateTestType(
            @PathVariable Long id,
            @Valid @RequestBody TestTypeRequestDTO testTypeDTO
    ) {
        TestTypeResponseDTO updatedTestType = testTypeServiceImpl.updateTestType(id, testTypeDTO);
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
        testTypeServiceImpl.deleteTestType(id);
        return ResponseEntity.noContent().build();
    }
}