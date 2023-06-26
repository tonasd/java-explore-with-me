package ru.practicum.ewm.service;

import ru.practicum.ewm.dto.event.EventFullDto;
import ru.practicum.ewm.dto.event.UpdateEventAdminRequest;
import ru.practicum.ewm.model.EventState;

import java.time.LocalDateTime;
import java.util.List;

public interface AdminEventService {
    List<EventFullDto> findEventsWith(List<Long> users, List<EventState> states,
                                      List<Integer> categories, LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                      int from, int size);

    EventFullDto updateEvent(long eventId, UpdateEventAdminRequest dto);
}
