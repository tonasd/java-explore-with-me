package ru.practicum.ewm.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ApiError handleWrongRequestParameters(final ConstraintViolationException e) {
        return ApiError.builder()
                .message(e.getMessage())
                .reason("Incorrectly made request")
                .status(HttpStatus.BAD_REQUEST)
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    protected ApiError handleDBConstrainsViolation(final DataIntegrityViolationException e) {
        return ApiError.builder()
                .status(HttpStatus.CONFLICT)
                .reason(e.getRootCause().getMessage())
                .message(e.getMessage())
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    protected ApiError handleNotFoundException(final NotFoundException e) {
        log.warn(e.getMessage());
        return ApiError.builder()
                .status(HttpStatus.NOT_FOUND)
                .reason("The required object was not found.")
                .message(e.getMessage())
                .build();
    }
}
