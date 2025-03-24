package com.lab.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.lab.validator.ValidName;
import jakarta.validation.constraints.*;
import com.lab.entity.Gender;
import com.lab.entity.Patient;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PatientDTO {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @NotBlank(message = "Необходима фамилия")
    @Size(max = 50, message = "Фамилия не должна превышать 50 символов")
    @ValidName
    private String lastName;

    @NotBlank(message = "Необходимо имя")
    @Size(max = 50, message = "Имя не должно превышать 50 символов")
    @ValidName
    private String firstName;

    @Size(max = 50, message = "Отчество не должно превышать 50 символов")
    @ValidName
    private String middleName;

    @NotNull(message = "Необходима дата рождения")
    @Past(message = "Дата рождения должна быть в прошлом")
    private LocalDate birthDate;

    @NotNull(message = "Необходим пол")
    private Gender gender;

    @NotNull(message = "Необходим номер телефона")
    private String phoneNumber;

    public PatientDTO(Patient patient) {
        this.id = patient.getId();
        this.lastName = patient.getLastName();
        this.firstName = patient.getFirstName();
        this.middleName = patient.getMiddleName();
        this.birthDate = patient.getBirthDate();
        this.gender = patient.getGender();
        this.phoneNumber = patient.getPhoneNumber();
    }
}
