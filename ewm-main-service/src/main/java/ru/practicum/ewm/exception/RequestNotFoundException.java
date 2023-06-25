package ru.practicum.ewm.exception;

public class RequestNotFoundException extends NotFoundException {
    public RequestNotFoundException(long requestId) {
        super(String.format("Request for participation with id=%d was not found", requestId));
    }
}
