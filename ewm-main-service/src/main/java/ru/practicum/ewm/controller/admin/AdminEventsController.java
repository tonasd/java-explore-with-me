package ru.practicum.ewm.controller.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.dto.event.EventFullDto;
import ru.practicum.ewm.dto.event.UpdateEventAdminRequest;
import ru.practicum.ewm.model.EventState;
import ru.practicum.ewm.service.AdminEventService;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/admin/events")
@RequiredArgsConstructor
public class AdminEventsController {
    private final AdminEventService service;

    @GetMapping
    public List<EventFullDto> findEvents(
            @RequestParam(required = false) List<Long> users, // список id пользователей, чьи события нужно найти
            @RequestParam(required = false) List<EventState> states, // список состояний в которых находятся искомые события
            @RequestParam(required = false) List<Integer> categories, // список id категорий в которых будет вестись поиск
            @RequestParam(required = false) LocalDateTime rangeStart, // дата и время не раньше которых должно произойти событие
            @RequestParam(required = false) LocalDateTime rangeEnd, // дата и время не позже которых должно произойти событие
            @RequestParam(defaultValue = "0") int from, // количество событий, которые нужно пропустить для формирования текущего набора
            @RequestParam(defaultValue = "10") int size // количество событий в наборе
            ) {
        log.info("GET /admin/events users={}, states={}, categories={}, rangeStart={}, rangeEnd={}, from={}, size={}",
                users, states, categories, rangeStart, rangeEnd, from, size);

        return service.findEventsWith(users, states, categories, rangeStart, rangeEnd, from, size);
    }

    @PatchMapping("/{eventId}")
    public EventFullDto updateEvent(@PathVariable long eventId, @RequestBody @Valid UpdateEventAdminRequest dto) {
        log.info("PATCH /admin/events/{} with {}", eventId, dto);

        return service.updateEvent(eventId, dto);
    }
}
