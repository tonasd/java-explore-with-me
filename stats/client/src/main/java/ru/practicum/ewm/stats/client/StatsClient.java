package ru.practicum.ewm.stats.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.stats.dto.CreationDto;
import ru.practicum.stats.dto.ViewDto;

import javax.validation.constraints.NotNull;
import java.net.InetAddress;
import java.net.URI;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class StatsClient {
    private final RestTemplate rest;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Autowired
    public StatsClient(@Value("${stats-server.url}") String serverUrl, RestTemplateBuilder builder) {
        rest = builder
                .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl))
                .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                .build();
    }

    public void saveRequest(@NotNull String appName,
                            @NotNull URI requestUri,
                            @NotNull InetAddress requesterIp,
                            @NotNull LocalDateTime timestamp) {
        CreationDto creationDto = new CreationDto();
        creationDto.setApp(appName);
        creationDto.setUri(requestUri);
        creationDto.setIp(requesterIp);
        creationDto.setTimestamp(timestamp);

        ResponseEntity<Void> response = rest.postForEntity("/hit", creationDto, void.class);

        if (response.getStatusCode().isError()) {
            // TODO: 19.06.2023 check if this suitable for main service
            throw new RuntimeException("Data for stats client is incorrect");
        }
    }

    public List<ViewDto> getStats(LocalDateTime start,
                                  LocalDateTime end,
                                  Boolean onlyForUniqueIps,
                                  List<URI> uris) {


        ViewDto[] resultArray = rest.getForObject("/stats?start={start}&end={end}&unique={unique}&uris={uris}",
                ViewDto[].class,
                prepareParameters(start, end, onlyForUniqueIps, uris));

        return resultArray != null ? Arrays.asList(resultArray) : List.of();
    }

    private Map<String, String> prepareParameters(LocalDateTime start,
                                                  LocalDateTime end,
                                                  Boolean unique,
                                                  List<URI> uris) {
        Map<String, String> parameters = new HashMap<>();

        parameters.put("start", start.format(formatter));
        parameters.put("end", end.format(formatter));

        parameters.put("unique", unique != null ? unique.toString() : null);

        if (uris != null && !uris.isEmpty()) {
            parameters.put("uris",
                    uris.stream().map(URI::toASCIIString).collect(Collectors.joining(",")));
        } else {
            parameters.put("uris", null);
        }

        return parameters;
    }
}
