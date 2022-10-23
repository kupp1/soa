package ru.ifmo.se.common.model;

import lombok.Data;

import java.util.Collections;
import java.util.Date;
import java.util.List;

@Data
public class ErrorResponse {
    private Date timestamp;
    private List<String> messages;

    public ErrorResponse(List<String> messages) {
        this.timestamp = new Date();
        this.messages = messages;
    }

    public ErrorResponse(String message) {
        new ErrorResponse(Collections.singletonList(message));
    }
}
