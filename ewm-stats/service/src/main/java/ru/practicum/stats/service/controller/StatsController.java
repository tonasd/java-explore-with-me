package ru.practicum.stats.service.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.stats.dto.CreationDto;
import ru.practicum.stats.dto.ViewDto;
import ru.practicum.stats.service.StatsService;

import javax.validation.Valid;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class StatsController {
    private final StatsService service;

    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public void saveRequest(@RequestBody @Valid CreationDto dto) {
        log.info("HitRecord {}", dto);
        service.save(dto);
    }

    @GetMapping("/stats")
    public List<ViewDto> getStats(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime start,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime end,
            @RequestParam(required = false)List<URI> uris,
            @RequestParam(defaultValue = "false") boolean unique
            ) {
        if (start.isAfter(end)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Start must be before end");
        }

        log.info("/stats?start={}&end={}&uris={}&unique={} ", start, end, uris, unique);
        return service.find(start, end, uris, unique);
    }

}
