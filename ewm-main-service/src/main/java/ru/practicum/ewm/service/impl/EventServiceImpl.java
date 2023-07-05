package ru.practicum.ewm.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.NotYetImplementedException;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.controller.pub.EventSort;
import ru.practicum.ewm.dto.event.EventFullDto;
import ru.practicum.ewm.dto.event.EventShortDto;
import ru.practicum.ewm.dto.event.NewEventDto;
import ru.practicum.ewm.dto.event.UpdateEventUserRequest;
import ru.practicum.ewm.exception.CategoryNotFoundException;
import ru.practicum.ewm.exception.EventNotFoundException;
import ru.practicum.ewm.exception.RulesViolationException;
import ru.practicum.ewm.exception.UserNotFoundException;
import ru.practicum.ewm.mapper.EventMapper;
import ru.practicum.ewm.model.Category;
import ru.practicum.ewm.model.Event;
import ru.practicum.ewm.model.EventState;
import ru.practicum.ewm.model.User;
import ru.practicum.ewm.repository.CategoryRepository;
import ru.practicum.ewm.repository.EventRepository;
import ru.practicum.ewm.repository.UserRepository;
import ru.practicum.ewm.service.EventHelper;
import ru.practicum.ewm.service.EventService;

import javax.persistence.Query;
import javax.persistence.criteria.*;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service("eventService")
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    protected final EventRepository eventRepository;
    protected final UserRepository userRepository;
    protected final CategoryRepository categoryRepository;
    protected final SessionFactory factory;
    protected final EventHelper helper;

    @Override
    public List<EventShortDto> findAll(
            String text,
            List<Integer> categories,
            Boolean paid,
            LocalDateTime rangeStart,
            LocalDateTime rangeEnd,
            boolean onlyAvailable,
            List<EventState> states,
            EventSort sort,
            int from,
            int size
    ) {

        List<Event> events = findByCriteria(text, categories, paid, rangeStart, rangeEnd, states, sort, from, size);
        helper.setConfirmedRequestsAndViews(events);

        List<EventShortDto> result = events.stream()
                //только события у которых не исчерпан лимит запросов на участие onlyAvailable
                .filter(event -> !onlyAvailable
                        || event.getParticipantLimit() == 0
                        || event.getConfirmedRequests() < event.getParticipantLimit())
                .map(EventMapper::mapToEventShortDto)
                .collect(Collectors.toUnmodifiableList());

        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventShortDto> findAllEventsOfUser(long userId, int from, int size) {
        checkUserExists(userId);
        PageRequest page = PageRequest.of(from / size, size);
        List<Event> events = eventRepository.findAllByInitiatorId(userId, page);

        helper.setConfirmedRequestsAndViews(events);

        return events.stream()
                .map(EventMapper::mapToEventShortDto)
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

        return EventMapper.mapToEventFullDto(newEvent);
    }

    @Override
    @Transactional(readOnly = true)
    public EventFullDto findEvent(long userId, long eventId) {
        checkUserExists(userId);
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException(eventId));

        if (event.getInitiator().getId() != userId) {
            log.info("Attempt to get full information about eventId={} from not initiator userId={}", event.getId(), userId);
            throw new EventNotFoundException(eventId);
        }
        helper.setConfirmedRequestsAndViews(List.of(event));
        helper.setRating(List.of(event));

        return EventMapper.mapToEventFullDto(event);
    }

    @Override
    @Transactional(readOnly = true)
    public EventFullDto findEvent(long id, EventState state) {
        Event event = eventRepository.findById(id).orElseThrow(() -> new EventNotFoundException(id));
        if (Objects.nonNull(state) && !state.equals(event.getState())) {
            throw new EventNotFoundException(id);
        }
        helper.setConfirmedRequestsAndViews(List.of(event));
        helper.setRating(List.of(event));

        return EventMapper.mapToEventFullDto(event);
    }

    @Override
    @Transactional
    public EventFullDto updateEvent(long userId, long eventId, UpdateEventUserRequest dto) {
        checkUserExists(userId);
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException(eventId));
        if (event.getInitiator().getId() != userId) {
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

        helper.setConfirmedRequestsAndViews(List.of(event));
        return EventMapper.mapToEventFullDto(event);
    }

    @Transactional(propagation = Propagation.MANDATORY)
    void checkUserExists(long userId) {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException(userId);
        }
    }

    /* based on:
     * Criteria API  https://www.baeldung.com/hibernate-criteria-queries
     * Pagination https://www.baeldung.com/jpa-pagination
     */
    private List<Event> findByCriteria(
            String text,
            List<Integer> categories,
            Boolean paid,
            LocalDateTime rangeStart,
            LocalDateTime rangeEnd,
            List<EventState> states,
            EventSort sort,
            int from,
            int size
    ) {
        Session session = factory.openSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Event> cr = cb.createQuery(Event.class);
        Root<Event> root = cr.from(Event.class);

        // Parsing parameters
        List<Predicate> predicates = new LinkedList<>();

        /*Based on https://stackoverflow.com/questions/4580285/jpa2-case-insensitive-like-matching-anywhere/47477470#47477470*/
        if (Objects.nonNull(text) && !text.isBlank()) {
            Expression<String> literal = cb.literal("%" + text + "%");
            Predicate textAnnotation = cb.like(cb.lower(root.get("annotation")), cb.lower(literal));
            Predicate textDescription = cb.like(cb.lower(root.get("description")), cb.lower(literal));
            predicates.add(cb.or(textAnnotation, textDescription));
        }
        if (Objects.nonNull(categories) && !categories.isEmpty()) {
            Predicate categoryId = root.get("category").get("id").in(categories);
            predicates.add(categoryId);
        }
        if (Objects.nonNull(paid)) {
            if (paid) {
                predicates.add(cb.isTrue(root.get("paid")));
            } else {
                predicates.add(cb.isFalse(root.get("paid")));
            }
        }
        if (Objects.nonNull(rangeStart)) {
            Predicate eventStarts = cb.greaterThanOrEqualTo(root.get("eventDate"), rangeStart);
            predicates.add(eventStarts);
        }
        if (Objects.nonNull(rangeEnd)) {
            Predicate eventEnds = cb.lessThanOrEqualTo(root.get("eventDate"), rangeEnd);
            predicates.add(eventEnds);
        }
        if (Objects.nonNull(states) && !states.isEmpty()) {
            Predicate state = root.get("state").in(states);
            predicates.add(state);
        }
        if (Objects.nonNull(sort)) {
            switch (sort) {
                case EVENT_DATE:
                    cr.orderBy(cb.desc(root.get("eventDate")));
                    break;
                case VIEWS:
                default:
                    throw new NotYetImplementedException();
            }
        }

        if (!predicates.isEmpty()) {
            cr.select(root).where(predicates.toArray(new Predicate[0]));
        }

        Query query = session.createQuery(cr);

        //pagination
        query.setFirstResult(from);
        query.setMaxResults(size);

        List<Event> results = query.getResultList();
        session.close();
        return results;
    }
}
