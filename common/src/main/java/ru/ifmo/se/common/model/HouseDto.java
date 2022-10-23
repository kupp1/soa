package ru.ifmo.se.common.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class HouseDto {
    private String name;
    private Integer year;
    private Long numberOfLifts;
}
