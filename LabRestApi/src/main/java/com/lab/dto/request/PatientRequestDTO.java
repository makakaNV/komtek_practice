package com.lab.dto.request;

import com.lab.entity.Gender;
import com.lab.validator.ValidName;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;

@Schema(description = "Запрос DTO для создания и обновления пациента")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PatientRequestDTO {

    @Schema(description = "Фамилия пациента", example = "Иванов")
    @NotBlank(message = "Необходима фамилия")
    @Size(max = 50, message = "Фамилия не должна превышать 50 символов")
    @ValidName
    private String lastName;

    @Schema(description = "Имя пациента", example = "Иван")
    @NotBlank(message = "Необходимо имя")
    @Size(max = 50, message = "Имя не должно превышать 50 символов")
    @ValidName
    private String firstName;

    @Schema(description = "Отчество пациента", example = "Иванович")
    @Size(max = 50, message = "Отчество не должно превышать 50 символов")
    @ValidName
    private String middleName;

    @Schema(description = "Дата рождения пациента yyyy-mm-dd", example = "1991-01-01")
    @NotNull(message = "Необходима дата рождения")
    @Past(message = "Дата рождения должна быть в прошлом")
    private LocalDate birthDate;

    @Schema(description = "Пол пациента", example = "MALE")
    @NotNull(message = "Необходим пол")
    private Gender gender;

    @Schema(description = "Номер телефона", example = "+79333010203")
    @NotNull(message = "Необходим номер телефона")
    private String phoneNumber;

    @Schema(description = "Номер СНИЛС", example = "213123")
    @NotNull(message = "Необходим номер СНИЛС")
    private String snils;
}

