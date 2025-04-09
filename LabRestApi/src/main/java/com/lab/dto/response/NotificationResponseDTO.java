package com.lab.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationResponseDTO {

    @Schema(description = "Id уведомления", example = "1")
    private Long id;

    @Schema(description = "Id измененной заявки", example = "1")
    private Long orderId;

    @Schema(
            description = "Сообщение о изменениях в заявке",
            example = "Статус заявки 11 изменён на COMPLETED"
    )
    private String message;

    @Schema(
            description = "Дата получения уведомления",
            example = "Apr 9, 2025, 4:51:16 PM"
    )
    private LocalDateTime timestamp;
}
