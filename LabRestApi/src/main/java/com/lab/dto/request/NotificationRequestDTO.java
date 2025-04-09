package com.lab.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NotificationRequestDTO {

    @Schema(description = "ID измененной заявки")
    @NotNull(message = "orderId не должен быть пустым")
    private Long orderId;

    @Schema(description = "Сообщение о изменениях в заявке")
    @NotNull(message = "message не должен быть пустым")
    private String message;
}
