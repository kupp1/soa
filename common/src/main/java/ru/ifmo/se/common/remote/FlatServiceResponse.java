package ru.ifmo.se.common.remote;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.ifmo.se.common.model.FlatPageResponse;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class FlatServiceResponse implements Serializable {
    private Integer httpResponseCode;
    private FlatPageResponse response;
}
