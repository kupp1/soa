package ru.ifmo.se.common.exception;

import java.util.List;

public class InvalidEntityIdException extends AbstractRestException {
    public InvalidEntityIdException(List<String> messages) {
        super(messages);
    }

    public InvalidEntityIdException(String message) {
        super(message);
    }
}
