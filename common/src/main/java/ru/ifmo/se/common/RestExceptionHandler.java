package ru.ifmo.se.common;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler({ConstraintViolationException.class})
    protected ResponseEntity<Object> handleBadRequest(RuntimeException e, WebRequest request) {
        return createResponse(e, HttpStatus.BAD_REQUEST, request);
    }

    private ResponseEntity<Object> createResponse(RuntimeException e, HttpStatus httpStatus, WebRequest request) {
        return handleExceptionInternal(e, "", new HttpHeaders(), httpStatus, request);
    }
}
