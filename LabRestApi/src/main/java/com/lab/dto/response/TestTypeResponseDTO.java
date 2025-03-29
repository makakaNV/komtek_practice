package com.lab.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.math.BigDecimal;

@Schema(description = "Ответ DTO для просмотра типов лабораторных исследований")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TestTypeResponseDTO {

    @Schema(description = "ID типа исследования")
    private Long id;

    @Schema(description = "Название лабораторного исследования", example = "Анализ крови")
    private String name;

    @Schema(description = "Код лабораторного исследования", example = "BTG-03")
    private String code;

    @Schema(description = "Описание лабораторного исследования", example = "Общее описание исследования")
    private String description;

    @Schema(description = "Цена", example = "10.54")
    private BigDecimal price;
}
