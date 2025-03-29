package com.lab.dto.request;

import com.lab.entity.Status;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.*;
import lombok.*;

@Schema(description = "Запрос DTO для создания и обновления заявки")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderRequestDTO {

    @Schema(description = "ID пациента")
    @NotNull(message = "patientId не должен быть пустым")
    @Min(value = 1, message = "patientId должен быть не меньше 1")
    @Max(value = Long.MAX_VALUE, message = "patientId должен быть не больше типа Long")
    private Long patientId;

    @Schema(description = "Статус заявки", example = "CANCELED")
    @NotNull(message = "Необходим пол")
    @Enumerated(EnumType.STRING)
    private Status status;

    @Schema(description = "Комментарий к заявке", example = "комментарий")
    @Size(max = 500, message = "comment не должен превышать 500 символов")
    private String comment;
}
