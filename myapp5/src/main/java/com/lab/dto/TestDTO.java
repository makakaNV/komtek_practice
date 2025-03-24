package com.lab.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.lab.entity.Test;
import com.lab.entity.TestStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TestDTO {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @NotNull(message = "Необходим orderId")
    @Min(value = 1, message = "orderId должен быть не меньше 1")
    @Max(value = 1000000, message = "orderId должен быть не больше 1 000 000")
    private Long orderId;

    @Min(value = 1, message = "testTypeId должен быть не меньше 1")
    @Max(value = 1000000, message = "testTypeId должен быть не больше 1 000 000")
    @NotNull(message = "Необходим testTypeId")
    private Long testTypeId;

    @NotNull(message = "Необходима executionDate")
    @PastOrPresent(message = "executionDate должна быть в настоящем/прошлом")
    private LocalDateTime executionDate;

    @NotNull(message = "Необходим результат")
    @Size(max = 500, message = "Результат не может превышать 500 символов")
    private String result;

    @NotNull(message = "Необходим referenceValues")
    @Size(max = 500, message = "referenceValues не может превышать 500 символов")
    private String referenceValues;

    @NotNull(message = "Необходим статус")
    @Enumerated(EnumType.STRING)
    private TestStatus status;

    public TestDTO(Test test) {
        this.id = test.getId();
        this.orderId = test.getOrder().getId();
        this.testTypeId = test.getTestType().getId();
        this.executionDate = test.getExecutionDate();
        this.result = test.getResult();
        this.referenceValues = test.getReferenceValues();
        this.status = test.getStatus();
    }
}

