package ru.ifmo.se.common.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FlatPageResponse {
    private Integer page;
    private Integer pageSize;
    private Integer totalPages;
    private Long totalFlats;
    private Iterable<FlatDto> flats;
}
