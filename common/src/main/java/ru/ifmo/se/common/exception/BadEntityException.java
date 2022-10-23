package ru.ifmo.se.common.exception;

import java.util.List;

public class BadEntityException extends AbstractRestException {
    public BadEntityException(List<String> messages) {
        super(messages);
    }

    public BadEntityException(String message) {
        super(message);
    }
}
