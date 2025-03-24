package com.lab.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String lastName;

    private String firstName;

    private String middleName;

    private LocalDate birthDate;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private String phoneNumber;

    private String SNILS;

}
