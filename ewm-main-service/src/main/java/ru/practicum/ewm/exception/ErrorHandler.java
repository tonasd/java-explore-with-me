package ru.practicum.ewm.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.validation.ConstraintViolationException;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler({ConstraintViolationException.class, MethodArgumentTypeMismatchException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ApiError handleWrongRequestParameters(final RuntimeException e) {
        log.debug("Получен статус 400 Bad request {}", e.getMessage(), e);
        return ApiError.builder()
                .message(e.getMessage())
                .reason("Incorrectly made request")
                .status(HttpStatus.BAD_REQUEST)
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    protected ApiError handleDBConstrainsViolation(final DataIntegrityViolationException e) {
        log.debug("Получен статус 409 Conflict {}", e.getMessage(), e);
        return ApiError.builder()
                .status(HttpStatus.CONFLICT)
                .reason(e.getRootCause().getMessage())
                .message(e.getMessage())
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    protected ApiError handleNotFoundException(final NotFoundException e) {
        log.debug("Получен статус 404 Not found {}", e.getMessage(), e);
        return ApiError.builder()
                .status(HttpStatus.NOT_FOUND)
                .reason("The required object was not found.")
                .message(e.getMessage())
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ApiError handleWrongDtoFieldValue(final MethodArgumentNotValidException e) {
        log.debug("Получен статус 400 Bad request {}", e.getMessage(), e);
        return ApiError.builder()
                .status(HttpStatus.BAD_REQUEST)
                .reason("Incorrectly made request.")
                .message(String.format("Field: %s. Error: %s. Value: %s",
                        e.getFieldError().getField(),
                        e.getFieldError().getDefaultMessage(),
                        String.valueOf(e.getFieldError().getRejectedValue())))
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    protected ApiError handleRulesViolation(final RulesViolationException e) {
        log.debug("Получен статус 409 Conflict {}", e.getMessage(), e);
        return ApiError.builder()
                .message(e.getMessage())
                .reason("For the requested operation the conditions are not met.")
                .status(HttpStatus.CONFLICT)
                .build();
    }
}
