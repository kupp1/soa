package ru.ifmo.se.common.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FlatPageResponse implements Serializable {
    private Integer page;
    private Integer pageSize;
    private Integer totalPages;
    private Long totalFlats;
    private Iterable<FlatDto> flats;
}
