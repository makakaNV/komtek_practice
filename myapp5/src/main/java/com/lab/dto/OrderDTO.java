package com.lab.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.lab.entity.Order;
import com.lab.entity.Status;
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
public class OrderDTO {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @NotNull(message = "patientId не должен быть пустым")
    @Min(value = 1, message = "patientId должен быть не меньше 1")
    @Max(value = 1000000, message = "patientId должен быть не больше 1 000 000")
    private Long patientId;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdDate;

    @NotBlank(message = "Необходим пол")
    @Enumerated(EnumType.STRING)
    private Status status;

    @Size(max = 500, message = "comment не должен превышать 500 символов")
    private String comment;

    public OrderDTO(Order order){
        this.id = order.getId();
        this.patientId = order.getPatient().getId();
        this.createdDate = order.getCreatedDate();
        this.status = order.getStatus();
        this.comment = order.getComment();
    }
}
