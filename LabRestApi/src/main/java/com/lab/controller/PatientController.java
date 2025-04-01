package com.lab.controller;

import com.lab.dto.request.PatientRequestDTO;
import com.lab.dto.response.OrderResponseDTO;
import com.lab.dto.response.PatientResponseDTO;
import com.lab.exception.ErrorResponse;
import com.lab.service.impl.PatientServiceImpl;
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
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/patients")
@Tag(name = "Patients", description = "Управление пациентами")
public class PatientController {

    private final PatientServiceImpl patientServiceImpl;

    public PatientController(PatientServiceImpl patientServiceImpl) {
        this.patientServiceImpl = patientServiceImpl;
    }

    @GetMapping
    @SuppressWarnings("unused")
    @Operation(
            summary = "Получить всех пациентов",
            description = "Возвращает список всех пациентов. Реализована пагинация: по умолчанию page=0 & size=50")
    @ApiResponse(
            responseCode = "200",
            description = "Список пациентов успешно получен",
            content = @Content(schema = @Schema(implementation = PatientResponseDTO.class))
    )
    @ApiResponse(
            responseCode = "404",
            description = "Пациенты не найдены",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
    )
    public ResponseEntity<List<PatientResponseDTO>> getAllPatients(
            @ParameterObject
            @PageableDefault(size = 50)
            Pageable pageable
    ) {
        Page<PatientResponseDTO> patients = patientServiceImpl.getAllPatients(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(patients, "/api/v1/patients");
        return new ResponseEntity<>(patients.getContent(), headers, HttpStatus.OK);
    }


    @GetMapping("/{id}")
    @SuppressWarnings("unused")
    @Operation(summary = "Получить пациента по ID", description = "Возвращает пациента по указанному идентификатору")
    @ApiResponse(
            responseCode = "200",
            description = "Пациент успешно найден",
            content = @Content(schema = @Schema(implementation = PatientResponseDTO.class))
    )
    @ApiResponse(
            responseCode = "404",
            description = "Пациент не найден",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
    )
    public ResponseEntity<PatientResponseDTO> getPatient(@PathVariable Long id) {
        PatientResponseDTO patientDTO = patientServiceImpl.getPatient(id);
        return ResponseEntity.ok(patientDTO);
    }


    @PostMapping
    @SuppressWarnings("unused")
    @Operation(summary = "Создать нового пациента", description = "Создает нового пациента" +
            " на основе переданных данных")
    @ApiResponse(
            responseCode = "201",
            description = "Пациент успешно создан",
            content = @Content(schema = @Schema(implementation = PatientResponseDTO.class))
    )
    @ApiResponse(
            responseCode = "400",
            description = "Некорректные данные пациента",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
    )
    public ResponseEntity<PatientResponseDTO> createPatient(@Valid @RequestBody PatientRequestDTO patientDTO) {
        PatientResponseDTO createdPatient = patientServiceImpl.createPatient(patientDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPatient);
    }


    @PutMapping("/{id}")
    @SuppressWarnings("unused")
    @Operation(summary = "Обновить пациента", description = "Обновляет данные пациента по указанному идентификатору")
    @ApiResponse(
            responseCode = "200",
            description = "Пациент успешно обновлен",
            content = @Content(schema = @Schema(implementation = PatientResponseDTO.class))
    )
    @ApiResponse(
            responseCode = "400",
            description = "Некорректные данные пациента",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
    )
    @ApiResponse(
            responseCode = "404",
            description = "Пациент не найден",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
    )
    public ResponseEntity<PatientResponseDTO> updatePatient(
            @PathVariable Long id,
            @Valid @RequestBody PatientRequestDTO patientDTO
    ) {
        PatientResponseDTO updatedPatient = patientServiceImpl.updatePatient(id, patientDTO);
        return ResponseEntity.ok(updatedPatient);
    }



    @GetMapping("/search")
    @SuppressWarnings("unused")
    @Operation(summary = "Поиск пациентов по ФИО", description = "Возвращает список пациентов," +
            " соответствующих указанным параметрам поиска")
    @ApiResponse(
            responseCode = "200",
            description = "Список пациентов успешно получен",
            content = @Content(schema = @Schema(implementation = PatientResponseDTO.class))
    )
    @ApiResponse(
            responseCode = "404",
            description = "Пациент не найден",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
    )
    public ResponseEntity<List<PatientResponseDTO>> searchPatients(
            @RequestParam(required = false) String lastName,
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String middleName) {
        List<PatientResponseDTO> patients = patientServiceImpl.searchPatients(lastName, firstName, middleName);
        return ResponseEntity.ok(patients);
    }


    @GetMapping("/search-by-birthdate")
    @SuppressWarnings("unused")
    @Operation(summary = "Поиск пациентов по дате рождения", description = "Возвращает список пациентов," +
            " родившихся в указанную дату")
    @ApiResponse(
            responseCode = "200",
            description = "Список пациентов успешно получен",
            content = @Content(schema = @Schema(implementation = PatientResponseDTO.class))
    )
    @ApiResponse(
            responseCode = "404",
            description = "Пациент не найден",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
    )
    public ResponseEntity<List<PatientResponseDTO>> searchPatientsByBirthDate(@RequestParam LocalDate birthDate) {
        List<PatientResponseDTO> patients = patientServiceImpl.searchPatientsByBirthDate(birthDate);
        return ResponseEntity.ok(patients);
    }

    @GetMapping("/search-by-all")
    @SuppressWarnings("unused")
    @Operation(
            summary = "Поиск пациентов по ФИО и/или дате рождения",
            description = "Возвращает список пациентов по заданным параметрам поиска"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Список пациентов успешно получен",
            content = @Content(schema = @Schema(implementation = PatientResponseDTO.class))
    )
    @ApiResponse(
            responseCode = "404",
            description = "Пациент не найден",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
    )
    public ResponseEntity<List<PatientResponseDTO>> searchPatients(
            @RequestParam(required = false) String lastName,
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String middleName,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate birthDate) {

        List<PatientResponseDTO> patients = patientServiceImpl.searchPatients(
                lastName,
                firstName,
                middleName,
                birthDate
        );
        return ResponseEntity.ok(patients);
    }

    @GetMapping("/{patientId}/orders")
    @SuppressWarnings("unused")
    @Operation(
            summary = "Получить заказы пациента",
            description = "Возвращает список заказов для указанного пациента"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Список заказов успешно получен",
            content = @Content(schema = @Schema(implementation = OrderResponseDTO.class))
    )
    @ApiResponse(
            responseCode = "404",
            description = "Пациент не найден",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
    )
    public ResponseEntity<List<OrderResponseDTO>> getPatientOrders(@PathVariable Long patientId) {
        List<OrderResponseDTO> orders = patientServiceImpl.getPatientOrders(patientId);
        return ResponseEntity.ok(orders);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить пациента", description = "Удаляет пациента по указанному идентификатору")
    @ApiResponse(
            responseCode = "204",
            description = "Пациент успешно удален"
    )
    @ApiResponse(
            responseCode = "404",
            description = "Пациент не найден",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
    )
    public ResponseEntity<Void> deletePatient(@PathVariable Long id) {
        patientServiceImpl.deletePatient(id);
        return ResponseEntity.noContent().build();
    }

}
