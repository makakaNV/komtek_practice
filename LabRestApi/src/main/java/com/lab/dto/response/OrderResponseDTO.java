package com.lab.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lab.entity.Status;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

@Schema(description = "Ответ DTO для просмотра заявки")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderResponseDTO {

    @Schema(description = "ID заявки")
    private Long id;

    @Schema(description = "ID пациента")
    private Long patientId;

    @Schema(description = "Дата создания заявки yyyy-MM-dd HH:mm:ss", example = "1991-01-01 13:14:59")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdDate;

    @Schema(description = "Статус заявки", example = "CANCELED")
    private Status status;

    @Schema(description = "Комментарий к заявке", example = "комментарий")
    private String comment;
}
