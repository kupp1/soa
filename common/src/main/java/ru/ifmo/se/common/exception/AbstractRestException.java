package ru.ifmo.se.common.exception;

import lombok.Getter;

import java.util.Collections;
import java.util.List;

public abstract class AbstractRestException extends RuntimeException {
    @Getter
    private final List<String> messages;

    public AbstractRestException(List<String> messages) {
        super(String.join(", ", messages));
        this.messages = messages;
    }

    public AbstractRestException(String message) {
        super(message);
        this.messages = Collections.singletonList(message);
    }
}
