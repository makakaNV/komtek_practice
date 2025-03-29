package com.lab.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.math.BigDecimal;

@Schema(description = "Запрос DTO для создания и обновления типа лабораторного исследования")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TestTypeRequestDTO {

    @Schema(description = "Название лабораторного исследования", example = "Анализ крови")
    @NotNull(message = "Необходимо название")
    @Size(min = 1, max = 100, message = "Название должно быть не больше 100 символов")
    private String name;

    @Schema(description = "Код лабораторного исследования", example = "BTG-03")
    @Size(min = 1, max = 100, message = "Код не должен превышать 100 символов")
    private String code;

    @Schema(description = "Описание лабораторного исследования", example = "Общее описание исследования")
    @Size(max = 500, message = "Описание не должно превышать 500 символов")
    private String description;

    @Schema(description = "Цена", example = "10.54")
    @NotNull(message = "Необходима цена")
    private BigDecimal price;
}
