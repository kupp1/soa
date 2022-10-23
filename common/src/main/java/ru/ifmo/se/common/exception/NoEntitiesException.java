package ru.ifmo.se.common.exception;

import java.util.List;

public class NoEntitiesException extends AbstractRestException {
    public NoEntitiesException(List<String> messages) {
        super(messages);
    }

    public NoEntitiesException(String message) {
        super(message);
    }
}
