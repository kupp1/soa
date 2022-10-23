package ru.ifmo.se.common;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ru.ifmo.se.common.exception.AbstractRestException;
import ru.ifmo.se.common.exception.BadEntityException;
import ru.ifmo.se.common.exception.InvalidEntityIdException;
import ru.ifmo.se.common.exception.NoEntitiesException;
import ru.ifmo.se.common.model.ErrorResponse;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler({BadEntityException.class, InvalidEntityIdException.class})
    protected ResponseEntity<Object> handleBadEntity(AbstractRestException e, WebRequest request) {
        return sendResponse(e, request, HttpStatus.BAD_REQUEST, new ErrorResponse(e.getMessages()));
    }

    @ExceptionHandler({NoEntitiesException.class})
    protected ResponseEntity<Object> handleNotFound(AbstractRestException e, WebRequest request) {
        return sendResponse(e, request, HttpStatus.NOT_FOUND, new ErrorResponse(e.getMessages()));
    }

    private ResponseEntity<Object> sendResponse(RuntimeException e, WebRequest request, HttpStatus status,
                                                ErrorResponse errorResponse) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        return handleExceptionInternal(e, errorResponse, httpHeaders, status, request);
    }
}
