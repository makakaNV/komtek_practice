package com.lab.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.lab.entity.TestType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TestTypeDTO {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @NotNull(message = "Необходимо название")
    @Size(min = 1, max = 100, message = "Название должно быть не больше 100 символов")
    private String name;

    @Size(min = 1, max = 100, message = "Код не должен превышать 100 символов")
    private String code;

    @Size(max = 500, message = "Описание не должно превышать 500 символов")
    private String description;

    @NotNull(message = "Необходима цена")
    private BigDecimal price;

    public TestTypeDTO(TestType testType) {
        this.id = testType.getId();
        this.name = testType.getName();
        this.code = testType.getCode();
        this.description = testType.getDescription();
        this.price = testType.getPrice();
    }
}
