package com.lab.controller;

import com.lab.dto.OrderDTO;
import com.lab.dto.TestDTO;
import com.lab.exception.ErrorResponse;
import com.lab.service.OrderService;
import com.lab.entity.Status;
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
@RequestMapping("/api/v1/orders")
@Tag(name = "Orders", description = "Управление заявками")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }


    @GetMapping
    @Operation(summary = "Получить все заявки", description = "Возвращает список всех заявок")
    @ApiResponse(
            responseCode = "200",
            description = "Список заявок успешно получен",
            content = @Content(schema = @Schema(implementation = OrderDTO.class))
    )
    @ApiResponse(
            responseCode = "404",
            description = "Заявки не найдены",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
    )
    public ResponseEntity<List<OrderDTO>> getAllOrders() {
        List<OrderDTO> orders = orderService.getAllOrders();
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить заявку по ID", description = "Возвращает заявку по указанному ID")
    @ApiResponse(
            responseCode = "200",
            description = "Заявка успешна найдена",
            content = @Content(schema = @Schema(implementation = OrderDTO.class))
    )
    @ApiResponse(
            responseCode = "404",
            description = "Заявка не найдена",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
    )
    public ResponseEntity<OrderDTO> getOrder(@PathVariable Long id) {
        OrderDTO orderDTO = orderService.getOrderById(id);
        return ResponseEntity.ok(orderDTO);
    }

    @PostMapping
    @Operation(summary = "Создать новую заявку", description = "Создает новую заявку на основе переданных данных")
    @ApiResponse(
            responseCode = "201",
            description = "Заявка успешно создана",
            content = @Content(schema = @Schema(implementation = OrderDTO.class))
    )
    @ApiResponse(
            responseCode = "400",
            description = "Некорректные данные заявки",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
    )
    public ResponseEntity<OrderDTO> createOrder(@Valid @RequestBody OrderDTO orderDTO) {
        OrderDTO createdOrder = orderService.createOrder(orderDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdOrder);
    }


    @PutMapping("/{id}/status")
    @Operation(summary = "Обновить статус заявки", description = "Обновляет поле status у заявки")
    @ApiResponse(
            responseCode = "200",
            description = "Заявка успешно обновлена",
            content = @Content(schema = @Schema(implementation = OrderDTO.class))
    )
    @ApiResponse(
            responseCode = "404",
            description = "Заявка не найдена",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
    )
    public ResponseEntity<OrderDTO> updateOrderStatus(@PathVariable Long id, @Valid @RequestParam Status status) {
        OrderDTO updatedOrder = orderService.updateOrderStatus(id, status);
        return ResponseEntity.ok(updatedOrder);
    }


    @GetMapping("/patient/{patientId}")
    @Operation(summary = "Получить заявки по ID пациента", description = "Возвращает список заявок по ID пациента")
    @ApiResponse(
            responseCode = "200",
            description = "Список заявок успешно получен",
            content = @Content(schema = @Schema(implementation = OrderDTO.class))
    )
    @ApiResponse(
            responseCode = "404",
            description = "Пациент не найден",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
    )
    public ResponseEntity<List<OrderDTO>> getOrdersByPatientId(@PathVariable Long patientId) {
        List<OrderDTO> orders = orderService.getOrdersByPatientId(patientId);
        return ResponseEntity.ok(orders);
    }


    @GetMapping("/{orderId}/tests")
    @Operation(summary = "Получить лаб. исследования по ID заявки",
            description = "Возвращает список лаб. исследований по ID заявки")
    @ApiResponse(
            responseCode = "200",
            description = "Список заявок успешно получен",
            content = @Content(schema = @Schema(implementation = TestDTO.class))
    )
    @ApiResponse(
            responseCode = "404",
            description = "Лаб. исследования не найдены",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
    )
    public ResponseEntity<List<TestDTO>> getTestsByOrderId(@PathVariable Long orderId) {
        List<TestDTO> tests = orderService.getTestsByOrderId(orderId);
        return ResponseEntity.ok(tests);

    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удаление заявки", description = "Удаляет заявку по указанному идентификатору")
    @ApiResponse(
            responseCode = "204",
            description = "Заявка успешно удален"
    )
    @ApiResponse(
            responseCode = "404",
            description = "Заявка не найден",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
    )
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }
}