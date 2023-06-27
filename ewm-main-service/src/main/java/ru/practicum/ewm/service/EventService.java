package ru.practicum.ewm.service;

import ru.practicum.ewm.controller.pub.EventSort;
import ru.practicum.ewm.dto.event.*;
import ru.practicum.ewm.model.EventState;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

public interface EventService {
    List<EventShortDto> findAll(
            String text,
            List<Integer> categories,
            Boolean paid,
            LocalDateTime rangeStart,
            LocalDateTime rangeEnd,
            boolean onlyAvailable,
            List<EventState> states, EventSort sort,
            @PositiveOrZero int from,
            @Positive int size
    );

    List<EventShortDto> findAllEventsOfUser(long userId, int from, int size);

    EventFullDto createEvent(long userId, NewEventDto dto);

    EventFullDto findEvent(long userId, long eventId);

    EventFullDto findEvent(long id, EventState state);

    EventFullDto updateEvent(long userId, long eventId, UpdateEventUserRequest dto);

}
