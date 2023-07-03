package ru.practicum.ewm.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.model.Event;
import ru.practicum.ewm.model.RequestStatus;
import ru.practicum.ewm.repository.ParticipationRequestRepository;
import ru.practicum.ewm.repository.projection.RequestView;
import ru.practicum.ewm.stats.Stats;
import ru.practicum.ewm.stats.ViewShortDto;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventHelper {
    private final Stats stats;
    private final ParticipationRequestRepository requestRepository;

    @Transactional(readOnly = true)
    public void setConfirmedRequestsAndViews(List<Event> events) {
        //key = event id, value = counted confirmed requests
        Map<Long, Long> confirmedRequests = requestRepository.countRequests(
                        events.stream().map(Event::getId).collect(Collectors.toUnmodifiableList()),
                        RequestStatus.CONFIRMED).stream()
                .collect(Collectors.toMap(RequestView::getEventId, RequestView::getCount));
        //key = event id, value = counted views
        Map<Long, Long> views = stats.getViewsForEvents(events).stream()
                .collect(Collectors.toMap(ViewShortDto::getEventId, ViewShortDto::getViews));

        events.forEach(event -> {
            Long eventId = event.getId();
            event.setConfirmedRequests(confirmedRequests.getOrDefault(eventId, 0L));
            event.setViews(views.getOrDefault(eventId, 0L));
        });
    }
}
