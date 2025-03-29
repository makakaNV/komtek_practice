package com.lab.dto.response;

import com.lab.entity.TestStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

@Schema(description = "Ответ DTO для просмотра лабораторных исследований")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TestResponseDTO {

    @Schema(description = "ID лабораторного исследования")
    private Long id;

    @Schema(description = "ID заявки")
    private Long orderId;

    @Schema(description = "ID типа исследования")
    private Long testTypeId;

    @Schema(
            description = "Дата выполнения лабораторного исследования (yyyy-MM-dd HH:mm:ss)",
            example = "1991-01-01 13:14:59"
    )
    private LocalDateTime executionDate;

    @Schema(description = "Результат лабораторного исследования", example = "Результат положительный")
    private String result;

    @Schema(description = "Референсные значения для лабораторного исследования", example = "Значение")
    private String referenceValues;

    @Schema(description = "Статус лабораторного исследования", example = "PENDING")
    private TestStatus status;
}

