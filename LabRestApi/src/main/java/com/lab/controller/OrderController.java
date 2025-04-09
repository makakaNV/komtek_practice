package com.lab.controller;

import com.lab.dto.request.OrderRequestDTO;
import com.lab.dto.response.OrderResponseDTO;
import com.lab.dto.response.TestResponseDTO;
import com.lab.exception.ErrorResponse;
import com.lab.service.impl.OrderServiceImpl;
import com.lab.entity.Status;
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
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
@Tag(name = "Orders", description = "Управление заявками")
public class OrderController {

    private final OrderServiceImpl orderServiceImpl;

    public OrderController(OrderServiceImpl orderServiceImpl) {
        this.orderServiceImpl = orderServiceImpl;
    }


    @GetMapping
    @SuppressWarnings("unused")
    @Operation(
            summary = "Получить все заявки",
            description = "Возвращает список всех заявок. Реализована пагинация.",
            security = { @SecurityRequirement(name = "bearerAuth") }
    )
    @ApiResponse(
            responseCode = "200",
            description = "Список заявок успешно получен.",
            content = @Content(schema = @Schema(implementation = OrderResponseDTO.class))
    )
    @ApiResponse(
            responseCode = "404",
            description = "Заявки не найдены",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
    )
    @ApiResponse(
            responseCode = "401",
            description = "Требуется авторизация. Необходим токен.",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
    )
    public ResponseEntity<List<OrderResponseDTO>> getAllOrders(
            @ParameterObject
            @PageableDefault(size = 50)
            Pageable pageable
    ) {
        Page<OrderResponseDTO> orders = orderServiceImpl.getAllOrders(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(orders, "/api/v1/orders");
        return new ResponseEntity<>(orders.getContent(), headers, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @SuppressWarnings("unused")
    @Operation(
            summary = "Получить заявку по ID",
            description = "Возвращает заявку по указанному ID",
            security = { @SecurityRequirement(name = "bearerAuth") }
    )
    @ApiResponse(
            responseCode = "200",
            description = "Заявка успешна найдена",
            content = @Content(schema = @Schema(implementation = OrderResponseDTO.class))
    )
    @ApiResponse(
            responseCode = "404",
            description = "Заявка не найдена",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
    )
    public ResponseEntity<OrderResponseDTO> getOrder(@PathVariable Long id) {
        OrderResponseDTO orderDTO = orderServiceImpl.getOrderById(id);
        return ResponseEntity.ok(orderDTO);
    }

    @PostMapping
    @SuppressWarnings("unused")
    @Operation(
            summary = "Создать новую заявку",
            description = "Создает новую заявку на основе переданных данных",
            security = { @SecurityRequirement(name = "bearerAuth") }
    )
    @ApiResponse(
            responseCode = "201",
            description = "Заявка успешно создана",
            content = @Content(schema = @Schema(implementation = OrderRequestDTO.class))
    )
    @ApiResponse(
            responseCode = "400",
            description = "Некорректные данные заявки",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
    )
    public ResponseEntity<OrderResponseDTO> createOrder(
            @Valid @RequestBody OrderRequestDTO orderRequestDTO
    ) {
        OrderResponseDTO createdOrder = orderServiceImpl.createOrder(orderRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdOrder);
    }


    @PutMapping("/{id}/status")
    @SuppressWarnings("unused")
    @Operation(
            summary = "Обновить статус заявки",
            description = "Обновляет поле status у заявки",
            security = { @SecurityRequirement(name = "bearerAuth") })
    @ApiResponse(
            responseCode = "200",
            description = "Заявка успешно обновлена",
            content = @Content(schema = @Schema(implementation = OrderResponseDTO.class))
    )
    @ApiResponse(
            responseCode = "404",
            description = "Заявка не найдена",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
    )
    public ResponseEntity<OrderResponseDTO> updateOrderStatus(
            @PathVariable Long id,
            @Valid @RequestParam Status status
    ) {
        OrderResponseDTO updatedOrder = orderServiceImpl.updateOrderStatus(id, status);
        return ResponseEntity.ok(updatedOrder);
    }


    @GetMapping("/patient/{patientId}")
    @SuppressWarnings("unused")
    @Operation(
            summary = "Получить заявки по ID пациента",
            description = "Возвращает список заявок по ID пациента",
            security = { @SecurityRequirement(name = "bearerAuth") }
    )
    @ApiResponse(
            responseCode = "200",
            description = "Список заявок успешно получен",
            content = @Content(schema = @Schema(implementation = OrderResponseDTO.class))
    )
    @ApiResponse(
            responseCode = "404",
            description = "Пациент не найден",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
    )
    public ResponseEntity<List<OrderResponseDTO>> getOrdersByPatientId(@PathVariable Long patientId) {
        List<OrderResponseDTO> orders = orderServiceImpl.getOrdersByPatientId(patientId);
        return ResponseEntity.ok(orders);
    }


    @GetMapping("/{orderId}/tests")
    @SuppressWarnings("unused")
    @Operation(
            summary = "Получить лаб. исследования по ID заявки",
            description = "Возвращает список лаб. исследований по ID заявки",
            security = { @SecurityRequirement(name = "bearerAuth") }
    )
    @ApiResponse(
            responseCode = "200",
            description = "Список заявок успешно получен",
            content = @Content(schema = @Schema(implementation = TestResponseDTO.class))
    )
    @ApiResponse(
            responseCode = "404",
            description = "Лаб. исследования не найдены",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
    )
    public ResponseEntity<List<TestResponseDTO>> getTestsByOrderId(@PathVariable Long orderId) {
        List<TestResponseDTO> tests = orderServiceImpl.getTestsByOrderId(orderId);
        return ResponseEntity.ok(tests);

    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Удаление заявки",
            description = "Удаляет заявку по указанному идентификатору",
            security = { @SecurityRequirement(name = "bearerAuth") }
    )
    @ApiResponse(
            responseCode = "204",
            description = "Заявка успешно удалена"
    )
    @ApiResponse(
            responseCode = "404",
            description = "Заявка не найден",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
    )
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        orderServiceImpl.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }
}