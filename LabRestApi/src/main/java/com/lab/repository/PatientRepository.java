package com.lab.repository;

import com.lab.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;


public interface PatientRepository extends JpaRepository<Patient, Long> {
    List<Patient> findByLastNameAndFirstNameAndMiddleName(
            String lastName, String firstName, String middleName);

    List<Patient> findByBirthDate(LocalDate birthDate);

    List<Patient> findByLastNameAndFirstNameAndMiddleNameAndBirthDate(
            String lastName,
            String firstName,
            String middleName,
            LocalDate birthDate
    );
}
