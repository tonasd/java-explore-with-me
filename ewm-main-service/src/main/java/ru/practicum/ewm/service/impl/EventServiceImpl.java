package ru.practicum.ewm.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.dto.event.EventFullDto;
import ru.practicum.ewm.dto.event.EventShortDto;
import ru.practicum.ewm.dto.event.NewEventDto;
import ru.practicum.ewm.dto.event.UpdateEventUserRequest;
import ru.practicum.ewm.exception.CategoryNotFoundException;
import ru.practicum.ewm.exception.EventNotFoundException;
import ru.practicum.ewm.exception.RulesViolationException;
import ru.practicum.ewm.exception.UserNotFoundException;
import ru.practicum.ewm.mapper.EventMapper;
import ru.practicum.ewm.model.*;
import ru.practicum.ewm.repository.CategoryRepository;
import ru.practicum.ewm.repository.EventRepository;
import ru.practicum.ewm.repository.UserRepository;
import ru.practicum.ewm.repository.projection.EventShortView;
import ru.practicum.ewm.service.EventService;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    @Override
    @Transactional(readOnly = true)
    public List<EventShortDto> findAllEventsOfUser(long userId, int from, int size) {
        checkUserExists(userId);
        PageRequest page = PageRequest.of(from / size, size);
        List<EventShortView> events = eventRepository.findAllByInitiatorId(userId, page);
        return events.stream()
                .map(e -> EventMapper.mapToEventShortDto(e, getConfirmedRequests(e.getId()), getViews(e.getId())))
                .collect(Collectors.toUnmodifiableList());
    }

    @Override
    @Transactional
    public EventFullDto createEvent(long userId, NewEventDto dto) {
        User initiator = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        Category category = categoryRepository.findById(dto.getCategory())
                .orElseThrow(() -> new CategoryNotFoundException(dto.getCategory()));
        Event newEvent = EventMapper.mapToEvent(initiator, dto, category);
        newEvent = eventRepository.save(newEvent);

        return EventMapper.mapToEventFullDto(newEvent, 0, 0);
    }

    @Override
    @Transactional(readOnly = true)
    public EventFullDto findEvent(long userId, long eventId) {
        checkUserExists(userId);
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException(eventId));

        if(event.getInitiator().getId() != userId) {
            log.info("Attempt to get full information about eventId={} from not initiator userId={}", event.getId(), userId);
            throw new EventNotFoundException(eventId);
        }

        return EventMapper.mapToEventFullDto(event, getConfirmedRequests(eventId), getViews(eventId));
    }

    @Override
    @Transactional
    public EventFullDto updateEvent(long userId, long eventId, UpdateEventUserRequest dto) {
        checkUserExists(userId);
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException(eventId));
        if(event.getInitiator().getId() != userId) {
            log.info("Attempt to update eventId={} from not initiator userId={}", event.getId(), userId);
            throw new EventNotFoundException(eventId);
        }

        //изменить можно только отмененные события или события в состоянии ожидания модерации
        if (EventState.PUBLISHED.equals(event.getState())) {
            throw new RulesViolationException("Only pending or canceled events can be changed");
        }
        Category category = null;
        if (dto.getCategory() != null) {
            category = categoryRepository.findById(dto.getCategory())
                    .orElseThrow(() -> new CategoryNotFoundException(dto.getCategory()));
        }

        event = eventRepository.save(EventMapper.mapToEvent(event, dto, category));
        return EventMapper.mapToEventFullDto(event, getConfirmedRequests(eventId), getViews(eventId));
    }

    @Transactional(propagation = Propagation.MANDATORY)
    private void checkUserExists(long userId) {
        if(!userRepository.existsById(userId)) {
            throw new UserNotFoundException(userId);
        }
    }

    private long getViews(long eventId) {
        //TODO: implement
        return 0;
    }

    private long getConfirmedRequests(long eventId) {
        //TODO: implement
        return 0;
    }
}
