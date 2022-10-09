package ru.ifmo.se.common.entity;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Entity
public class House {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Min(1)
    @Max(808)
    private Integer year;

    @Min(1)
    private Long numberOfLifts;
}
