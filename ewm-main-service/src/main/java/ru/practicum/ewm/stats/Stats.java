package ru.practicum.ewm.stats;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.ewm.stats.client.StatsClient;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.URI;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class Stats {
    private static final String APP_NAME = "explore-with-me";
    private static final URI EVENT_URI = URI.create("/events/");
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
        statsClient.getStats(
                BEFORE_ALL,
                AFTER_ALL,
                true,
                List.of(EVENT_URI.resolve(Long.toString(eventId)))
        );
        return 0;
    }
}
