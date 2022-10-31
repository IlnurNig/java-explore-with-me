package ru.practicum.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.exception.exceptionClass.ExceptionBadRequest;
import ru.practicum.exception.exceptionClass.ExceptionConflict;
import ru.practicum.exception.exceptionClass.ExceptionInteralServerError;
import ru.practicum.exception.exceptionClass.ExceptionNotFound;
import ru.practicum.exception.model.ErrorResponse;


@RestControllerAdvice
@Slf4j
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse exceptionConflict(final ExceptionConflict e) {
        log.error(e.getMessage());
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse exceptionBadRequest(final ExceptionBadRequest e) {
        log.error(e.getMessage());
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse exceptionNotFound(final ExceptionNotFound e) {
        log.error(e.getMessage());
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse exceptionInternalServerError(final ExceptionInteralServerError e) {
        log.error(e.getMessage());
        return new ErrorResponse(e.getMessage());
    }

}
