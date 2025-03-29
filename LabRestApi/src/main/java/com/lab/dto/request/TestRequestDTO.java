package com.lab.dto.request;

import com.lab.entity.TestStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;


@Schema(description = "Запрос DTO для создания и обновления лабораторного исследования")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TestRequestDTO {

    @Schema(description = "ID заявки")
    @NotNull(message = "Необходим orderId")
    @Min(value = 1, message = "orderId должен быть не меньше 1")
    @Max(value = 1_000_000, message = "orderId должен быть не больше 1 000 000")
    private Long orderId;

    @Schema(description = "ID типа исследования")
    @NotNull(message = "Необходим testTypeId")
    @Min(value = 1, message = "testTypeId должен быть не меньше 1")
    @Max(value = 1_000_000, message = "testTypeId должен быть не больше 1 000 000")
    private Long testTypeId;

    @Schema(description = "Результат лабораторного исследования", example = "Результат положительный")
    @NotNull(message = "Необходим результат")
    @Size(max = 500, message = "Результат не может превышать 500 символов")
    private String result;

    @Schema(description = "Референсные значения для лабораторного исследования", example = "Значение")
    @NotNull(message = "Необходим referenceValues")
    @Size(max = 500, message = "referenceValues не может превышать 500 символов")
    private String referenceValues;

    @Schema(description = "Статус лабораторного исследования", example = "PENDING")
    @NotNull(message = "Необходим статус")
    private TestStatus status;
}

