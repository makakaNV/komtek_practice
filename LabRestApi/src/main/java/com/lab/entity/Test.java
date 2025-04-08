package com.lab.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "tests")
public class Test {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tests_id_seq")
    @SequenceGenerator(name = "tests_id_seq", sequenceName = "tests_id_seq", allocationSize = 1)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne
    @JoinColumn(name = "test_type_id")
    private TestType testType;

    @Column(name = "execution_date")
    private LocalDateTime executionDate;

    @Column(name = "result")
    private String result;

    @Column(name = "reference_values")
    private String referenceValues;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private TestStatus status;

}
