package com.lab.controller;


import com.lab.dto.request.NotificationRequestDTO;
import com.lab.entity.Notification;
import com.lab.exception.ErrorResponse;
import com.lab.mapper.impl.NotificationMapperImpl;
import com.lab.repository.NotificationRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;


@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Notifications", description = "Управление уведомлениями")
public class NotificationController {

    private final NotificationRepository notificationRepository;
    private final NotificationMapperImpl notificationMapper;

    @PostMapping
    @SuppressWarnings("unused")
    @Operation(
            summary = "Тестовый контроллер для приема уведомлений",
            description = "Тестовый контроллер для проверки взаимодействия сервисов. " +
                    "Сохраняет уведомление в БД"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Уведомление сохранено"
    )
    @ApiResponse(
            responseCode = "500",
            description = "Некая ошибка на стороне другого сервиса",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
    )
    public ResponseEntity<Void> receiveNotification(@RequestBody NotificationRequestDTO requestDTO) {
        Notification notification = notificationMapper.toEntity(requestDTO);
        notification.setTimestamp(LocalDateTime.now());
        notificationRepository.save(notification);
        return ResponseEntity.ok().build();
    }
}
