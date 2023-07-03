package ru.practicum.ewm.stats;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.practicum.ewm.model.Event;
import ru.practicum.ewm.stats.client.StatsClient;
import ru.practicum.stats.dto.ViewDto;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.URI;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class Stats {
    private static final String EVENT_URI = "/events/";
    private final String appName;
    private final StatsClient statsClient;

    public Stats(@Value("${stats-server.app_name}") String appName, StatsClient statsClient) {
        this.appName = appName;
        this.statsClient = statsClient;
    }

    public void saveRequest(HttpServletRequest request) throws UnknownHostException {
        statsClient.saveRequest(
                appName,
                URI.create(request.getRequestURI()),
                InetAddress.getByName(request.getRemoteAddr()),
                LocalDateTime.now()
        );
    }

    public long getViewsForEvent(Event event) {
        //if not published yet then no views
        if (Objects.isNull(event.getPublishedOn())) {
            return 0;
        }

        Optional<ViewDto> dto = statsClient.getStats(
                event.getPublishedOn(),
                LocalDateTime.now(),
                true,
                List.of(URI.create(EVENT_URI + event.getId()))
        ).stream().findFirst();

        return dto.map(ViewDto::getHits).orElse(0L);
    }

    // Возвращает только для тех, для которых количество просмотров отличается от 0
    public List<ViewShortDto> getViewsForEvents(Collection<Event> events) {
        if (events == null) {
            return List.of();
        }

        List<ViewShortDto> result = List.of();
        LocalDateTime start = events.stream()
                .filter(event -> event.getPublishedOn() != null)
                .min(Comparator.nullsLast(Comparator.comparing(Event::getPublishedOn)))
                .map(Event::getPublishedOn).orElse(null);

        // иначе все события не имеют даты публикации и нет смысла вообще запрашивать
        if (start != null) {
            List<URI> uris = events.stream()
                    .filter(e -> Objects.nonNull(e.getPublishedOn()))
                    .map(event -> URI.create(EVENT_URI + event.getId()))
                    .collect(Collectors.toList());
            result = statsClient.getStats(
                    start,
                    LocalDateTime.now(),
                    true,
                    uris).stream().map(viewDto -> {
                String path = viewDto.getUri().getPath();
                String idStr = path.substring(path.lastIndexOf('/') + 1);
                return new ViewShortDto(Long.parseLong(idStr), viewDto.getHits());
            }).collect(Collectors.toList());

        }

        return result;
    }
}
