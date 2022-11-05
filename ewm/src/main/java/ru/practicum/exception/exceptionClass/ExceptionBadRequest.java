package ru.practicum.exception.exceptionClass;

public class ExceptionBadRequest extends RuntimeException {
    public ExceptionBadRequest(String message) {
        super(message);
    }
}
