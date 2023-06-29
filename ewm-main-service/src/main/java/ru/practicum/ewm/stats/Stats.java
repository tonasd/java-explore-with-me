package ru.practicum.ewm.stats;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.ewm.stats.client.StatsClient;
import ru.practicum.stats.dto.ViewDto;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.URI;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class Stats {
    private static final String APP_NAME = "explore-with-me";
    private static final String EVENT_URI = "/events/";
    private static final LocalDateTime BEFORE_ALL = LocalDateTime.of(1970, 1, 1, 0, 0);
    private static final LocalDateTime AFTER_ALL = LocalDateTime.of(2970, 1, 1, 0, 0);
    private final StatsClient statsClient;

    public void saveRequest(HttpServletRequest request) throws UnknownHostException {
        statsClient.saveRequest(
                APP_NAME,
                URI.create(request.getRequestURI()),
                InetAddress.getByName(request.getRemoteAddr()),
                LocalDateTime.now()
        );
    }

    public long getViewsForEvent(long eventId) {
        Optional<ViewDto> dto = statsClient.getStats(
                BEFORE_ALL,
                AFTER_ALL,
                true,
                List.of(URI.create(EVENT_URI + eventId))
        ).stream().findFirst();

        return dto.map(ViewDto::getHits).orElse(0L);
    }
}
