package ru.practicum.ewm.exception;

public class EventNotFoundException extends NotFoundException {
    public EventNotFoundException(long eventId) {
        super(String.format("Event with id=%d was not found", eventId));
    }
}
