package ru.practicum.ewm.exception;

import java.util.List;

public class EventNotFoundException extends NotFoundException {
    public EventNotFoundException(long eventId) {
        super(String.format("Event with id=%d was not found", eventId));
    }

    public EventNotFoundException(List<Long> ids) {
        super(String.format("Event with ids=%s was not found", ids.toString()));
    }
}
