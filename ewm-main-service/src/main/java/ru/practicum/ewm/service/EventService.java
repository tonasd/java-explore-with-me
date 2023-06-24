package ru.practicum.ewm.service;

import ru.practicum.ewm.dto.event.EventFullDto;
import ru.practicum.ewm.dto.event.EventShortDto;
import ru.practicum.ewm.dto.event.NewEventDto;
import ru.practicum.ewm.dto.event.UpdateEventUserRequest;

import java.util.List;

public interface EventService {
    List<EventShortDto> findAllEventsOfUser(long userId, int from, int size);

    EventFullDto createEvent(long userId, NewEventDto dto);

    EventFullDto findEvent(long userId, long eventId);

    EventFullDto updateEvent(long userId, long eventId, UpdateEventUserRequest dto);
}
