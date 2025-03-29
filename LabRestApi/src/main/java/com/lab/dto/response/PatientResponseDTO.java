package com.lab.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.lab.entity.Gender;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDate;

@Schema(description = "Ответ DTO для просмотра пациентов")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PatientResponseDTO {

    @Schema(description = "ID пациента")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @Schema(description = "Фамилия пациента", example = "Иванов")
    private String lastName;

    @Schema(description = "Имя пациента", example = "Иван")
    private String firstName;

    @Schema(description = "Отчество пациента", example = "Иванович")
    private String middleName;

    @Schema(description = "Дата рождения пациента yyyy-mm-dd", example = "1991-01-01")
    private LocalDate birthDate;

    @Schema(description = "Пол пациента", example = "MALE")
    private Gender gender;

    @Schema(description = "Номер телефона", example = "+79333010203")
    private String phoneNumber;

    @Schema(description = "Номер СНИЛС", example = "213123")
    private String snils;
}
