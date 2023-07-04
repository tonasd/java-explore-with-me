package ru.practicum.ewm.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.model.Event;
import ru.practicum.ewm.model.RequestStatus;
import ru.practicum.ewm.repository.ParticipationRequestRepository;
import ru.practicum.ewm.repository.RatingRepository;
import ru.practicum.ewm.repository.projection.EventRatingView;
import ru.practicum.ewm.repository.projection.RequestView;
import ru.practicum.ewm.stats.Stats;
import ru.practicum.ewm.stats.ViewShortDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventHelper {
    private final Stats stats;
    private final ParticipationRequestRepository requestRepository;
    private final RatingRepository ratingRepository;

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

    @Transactional(readOnly = true)
    public void setRating(List<Event> events) {
        Map<Long, EventRatingView> ratings = ratingRepository.getRatingForEvents(events.stream()
                        // there can be no rating for not started yet event, no need to fetch data for them
                        .filter(event -> event.getEventDate().isBefore(LocalDateTime.now()))
                        .map(Event::getId).collect(Collectors.toUnmodifiableList()))
                .collect(Collectors.toMap(EventRatingView::getEventId, Function.identity()));

        events.forEach(event -> event.setRating(ratings.get(event.getId())));
    }

}
