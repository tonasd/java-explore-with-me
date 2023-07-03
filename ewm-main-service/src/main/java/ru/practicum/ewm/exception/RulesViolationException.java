package ru.practicum.ewm.exception;

public class RulesViolationException extends RuntimeException {
    public RulesViolationException(String reason) {
        super(reason);
    }
}
