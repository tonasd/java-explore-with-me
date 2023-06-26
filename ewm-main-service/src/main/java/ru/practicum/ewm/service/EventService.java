package ru.practicum.ewm.service;

import ru.practicum.ewm.dto.event.*;
import ru.practicum.ewm.model.EventState;

import java.time.LocalDateTime;
import java.util.List;

public interface EventService {
    List<EventShortDto> findAllEventsOfUser(long userId, int from, int size);

    EventFullDto createEvent(long userId, NewEventDto dto);

    EventFullDto findEvent(long userId, long eventId);

    EventFullDto updateEvent(long userId, long eventId, UpdateEventUserRequest dto);
}
