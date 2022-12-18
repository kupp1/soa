package ru.ifmo.se.common.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CoordinatesDto implements Serializable {
    private Float x;
    private Integer y;
}
