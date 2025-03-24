package com.lab.controller;

import com.lab.dto.OrderDTO;
import com.lab.dto.PatientDTO;
import com.lab.exception.ErrorResponse;
import com.lab.service.PatientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/patients")
@Tag(name = "Patients", description = "Управление пациентами")
public class PatientController {

    @Autowired
    private PatientService patientService;

    @GetMapping
    @Operation(summary = "Получить всех пациентов", description = "Возвращает список всех пациентов")
    @ApiResponse(
            responseCode = "200",
            description = "Список пациентов успешно получен",
            content = @Content(schema = @Schema(implementation = PatientDTO.class))
    )
    @ApiResponse(
            responseCode = "404",
            description = "Пациенты не найдены",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
    )
    public ResponseEntity<List<PatientDTO>> getAllPatients() {
        List<PatientDTO> patients = patientService.getAllPatients();
        return ResponseEntity.ok(patients);
    }


    @GetMapping("/{id}")
    @Operation(summary = "Получить пациента по ID", description = "Возвращает пациента по указанному идентификатору")
    @ApiResponse(
            responseCode = "200",
            description = "Пациент успешно найден",
            content = @Content(schema = @Schema(implementation = PatientDTO.class))
    )
    @ApiResponse(
            responseCode = "404",
            description = "Пациент не найден",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
    )
    public ResponseEntity<PatientDTO> getPatient(@PathVariable Long id) {
        PatientDTO patientDTO = patientService.getPatient(id);
        return ResponseEntity.ok(patientDTO);
    }


    @PostMapping
    @Operation(summary = "Создать нового пациента", description = "Создает нового пациента на основе переданных данных")
    @ApiResponse(
            responseCode = "201",
            description = "Пациент успешно создан",
            content = @Content(schema = @Schema(implementation = PatientDTO.class))
    )
    @ApiResponse(
            responseCode = "400",
            description = "Некорректные данные пациента",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
    )
    public ResponseEntity<PatientDTO> createPatient(@Valid @RequestBody PatientDTO patientDTO) {
        PatientDTO createdPatient = patientService.createPatient(patientDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPatient);
    }


    @PutMapping("/{id}")
    @Operation(summary = "Обновить пациента", description = "Обновляет данные пациента по указанному идентификатору")
    @ApiResponse(
            responseCode = "200",
            description = "Пациент успешно обновлен",
            content = @Content(schema = @Schema(implementation = PatientDTO.class))
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
    public ResponseEntity<PatientDTO> updatePatient(@PathVariable Long id, @Valid @RequestBody PatientDTO patientDTO) {
        PatientDTO updatedPatient = patientService.updatePatient(id, patientDTO);
        return ResponseEntity.ok(updatedPatient);
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
        patientService.deletePatient(id);
        return ResponseEntity.noContent().build();
    }


    @GetMapping("/search")
    @Operation(summary = "Поиск пациентов по ФИО", description = "Возвращает список пациентов, соответствующих указанным параметрам поиска")
    @ApiResponse(
            responseCode = "200",
            description = "Список пациентов успешно получен",
            content = @Content(schema = @Schema(implementation = PatientDTO.class))
    )
    @ApiResponse(
            responseCode = "404",
            description = "Пациент не найден",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
    )
    public ResponseEntity<List<PatientDTO>> searchPatients(
            @RequestParam(required = false) String lastName,
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String middleName) {
        List<PatientDTO> patients = patientService.searchPatients(lastName, firstName, middleName);
        return ResponseEntity.ok(patients);
    }


    @GetMapping("/{patientId}/orders")
    @Operation(summary = "Получить заказы пациента", description = "Возвращает список заказов для указанного пациента")
    @ApiResponse(
            responseCode = "200",
            description = "Список заказов успешно получен",
            content = @Content(schema = @Schema(implementation = OrderDTO.class))
    )
    @ApiResponse(
            responseCode = "404",
            description = "Пациент не найден",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
    )
    public ResponseEntity<List<OrderDTO>> getPatientOrders(@PathVariable Long patientId) {
        List<OrderDTO> orders = patientService.getPatientOrders(patientId);
        return ResponseEntity.ok(orders);
    }


    @GetMapping("/search-by-birthdate")
    @Operation(summary = "Поиск пациентов по дате рождения", description = "Возвращает список пациентов, родившихся в указанную дату")
    @ApiResponse(
            responseCode = "200",
            description = "Список пациентов успешно получен",
            content = @Content(schema = @Schema(implementation = PatientDTO.class))
    )
    @ApiResponse(
            responseCode = "404",
            description = "Пациент не найден",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
    )
    public ResponseEntity<List<PatientDTO>> searchPatientsByBirthDate(@RequestParam LocalDate birthDate) {
        List<PatientDTO> patients = patientService.searchPatientsByBirthDate(birthDate);
        return ResponseEntity.ok(patients);
    }
}
