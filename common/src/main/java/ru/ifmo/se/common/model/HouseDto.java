package ru.ifmo.se.common.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class HouseDto implements Serializable {
    private String name;
    private Integer year;
    private Long numberOfLifts;
}
