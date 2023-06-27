package ru.practicum.ewm.controller.pub;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.dto.event.CategoryDto;
import ru.practicum.ewm.dto.event.EventFullDto;
import ru.practicum.ewm.dto.event.EventShortDto;
import ru.practicum.ewm.model.EventState;
import ru.practicum.ewm.service.EventService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Slf4j
@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
@Validated
public class PublicEventController {
    private final EventService eventService;

    @GetMapping
    public List<EventShortDto> findAll(
            @RequestParam(required = false) @Size(max = 7000) String text,
            @RequestParam(required = false) List<@Positive Integer> categories,
            @RequestParam(required = false) Boolean paid,
            @RequestParam(required = false) LocalDateTime rangeStart,
            @RequestParam(required = false) LocalDateTime rangeEnd,
            @RequestParam(defaultValue = "false") boolean onlyAvailable,
            @RequestParam(required = false) EventSort sort,
            @RequestParam(defaultValue = "0") @PositiveOrZero int from,
            @RequestParam(defaultValue = "10") @Positive int size
    ) {
        log.info("GET /events text={}, categories={}, paid={}, rangeStart={}, rangeEnd={}," +
                        " onlyAvailable={}, sort={}, from={}, size={}",
                text, categories, paid, rangeStart, rangeEnd,
                onlyAvailable, sort, from, size);
//        если в запросе не указан диапазон дат [rangeStart-rangeEnd], то нужно выгружать события, которые произойдут позже текущей даты и времени
        if (Objects.isNull(rangeStart) && Objects.isNull(rangeEnd)) {
            rangeStart = LocalDateTime.now();
        }
        List<EventState> states = List.of(EventState.PUBLISHED); // должны быть только опубликованные события

        //TODO: информацию о том, что по этому эндпоинту был осуществлен и обработан запрос, нужно сохранить в сервисе статистики
        return eventService.findAll(text, categories, paid,
                rangeStart, rangeEnd, onlyAvailable,
                states, sort, from, size);
    }

    @GetMapping("/{id}")
    public EventFullDto findEvent(@PathVariable long id) {
        log.info("GET /events/{}", id);
        //TODO: информацию о том, что по этому эндпоинту был осуществлен и обработан запрос, нужно сохранить в сервисе статистики
        return eventService.findEvent(id, EventState.PUBLISHED);
    }

}
