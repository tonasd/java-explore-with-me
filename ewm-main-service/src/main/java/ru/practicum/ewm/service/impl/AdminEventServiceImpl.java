package ru.practicum.ewm.service.impl;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.dto.event.EventFullDto;
import ru.practicum.ewm.dto.event.UpdateEventAdminRequest;
import ru.practicum.ewm.exception.CategoryNotFoundException;
import ru.practicum.ewm.exception.EventNotFoundException;
import ru.practicum.ewm.exception.RulesViolationException;
import ru.practicum.ewm.mapper.EventMapper;
import ru.practicum.ewm.model.Category;
import ru.practicum.ewm.model.Event;
import ru.practicum.ewm.model.EventState;
import ru.practicum.ewm.repository.CategoryRepository;
import ru.practicum.ewm.repository.EventRepository;
import ru.practicum.ewm.repository.UserRepository;
import ru.practicum.ewm.service.AdminEventService;
import ru.practicum.ewm.service.EventHelper;

import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class AdminEventServiceImpl extends EventServiceImpl implements AdminEventService {
    @Autowired
    public AdminEventServiceImpl(EventRepository eventRepository, UserRepository userRepository, CategoryRepository categoryRepository, SessionFactory factory, EventHelper helper) {
        super(eventRepository, userRepository, categoryRepository, factory, helper);
    }

    private static void checkEventDate(LocalDateTime eventDate) {
        if (eventDate.isBefore(LocalDateTime.now().plusHours(1))) {
            throw new RulesViolationException("Event cannot starts less than an hour from now to be moderated");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventFullDto> findEventsWith(
            List<Long> users,
            List<EventState> states,
            List<Integer> categories,
            LocalDateTime rangeStart,
            LocalDateTime rangeEnd,
            int from,
            int size
    ) {
        List<Event> events = findByCriteria(users, states, categories, rangeStart, rangeEnd, from, size);

        helper.setConfirmedRequestsAndViews(events);
        helper.setRating(events);

        return events.stream()
                .map(EventMapper::mapToEventFullDto)
                .collect(Collectors.toUnmodifiableList());
    }

    @Override
    @Transactional
    public EventFullDto updateEvent(long eventId, UpdateEventAdminRequest dto) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException(eventId));

        // дата начала изменяемого события должна быть не ранее чем за час от даты публикации. (Ожидается код ошибки 409)
        if (dto.getEventDate() != null) {
            checkEventDate(dto.getEventDate());
        } else {
            checkEventDate(event.getEventDate());
        }

        // событие можно публиковать, только если оно в состоянии ожидания публикации (Ожидается код ошибки 409)
        UpdateEventAdminRequest.StateAction stateAction = dto.getStateAction();
        if (stateAction != null && stateAction.equals(UpdateEventAdminRequest.StateAction.PUBLISH_EVENT)
                && !event.getState().equals(EventState.PENDING)) {
            throw new RulesViolationException("EventId=" + eventId + " not in PENDING state, cannot be published");
        }
        // событие можно отклонить, только если оно еще не опубликовано (Ожидается код ошибки 409)
        if (stateAction != null && stateAction.equals(UpdateEventAdminRequest.StateAction.REJECT_EVENT)
                && !event.getState().equals(EventState.PENDING)) {
            throw new RulesViolationException("EventId=" + eventId + " not in PENDING state, cannot be rejected");
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

    /* based on:
     * Criteria API  https://www.baeldung.com/hibernate-criteria-queries
     * Pagination https://www.baeldung.com/jpa-pagination
     */
    private List<Event>
    findByCriteria(List<Long> users,
                   List<EventState> states,
                   List<Integer> categories,
                   LocalDateTime rangeStart,
                   LocalDateTime rangeEnd,
                   int from,
                   int size) {

        Session session = factory.openSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Event> cr = cb.createQuery(Event.class);
        Root<Event> root = cr.from(Event.class);

        // Parsing parameters
        List<Predicate> predicates = new LinkedList<>();

        if (Objects.nonNull(users) && !users.isEmpty()) {
            Predicate initiatorId = root.get("initiator").get("id").in(users);
            predicates.add(initiatorId);
        }
        if (Objects.nonNull(states) && !states.isEmpty()) {
            Predicate state = root.get("state").in(states);
            predicates.add(state);
        }
        if (Objects.nonNull(categories) && !categories.isEmpty()) {
            Predicate categoryId = root.get("category").get("id").in(categories);
            predicates.add(categoryId);
        }
        if (Objects.nonNull(rangeStart)) {
            Predicate eventStarts = cb.greaterThanOrEqualTo(root.get("eventDate"), rangeStart);
            predicates.add(eventStarts);
        }
        if (Objects.nonNull(rangeEnd)) {
            Predicate eventEnds = cb.lessThanOrEqualTo(root.get("eventDate"), rangeEnd);
            predicates.add(eventEnds);
        }

        if (!predicates.isEmpty()) {
            cr.select(root).where(predicates.toArray(new Predicate[0]));
        }

        // sorting
        cr.orderBy(cb.desc(root.get("createdOn")));

        Query query = session.createQuery(cr);
        //pagination
        query.setFirstResult(from);
        query.setMaxResults(size);

        List<Event> results = query.getResultList();
        session.close();
        return results;
    }
}

