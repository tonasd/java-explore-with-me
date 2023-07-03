package ru.practicum.ewm.controller.reg;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.dto.event.*;
import ru.practicum.ewm.service.EventService;
import ru.practicum.ewm.service.ParticipationRequestService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users/{userId}/events")
@RequiredArgsConstructor
@Validated
public class EventsController {
    private final EventService eventService;
    private final ParticipationRequestService requestService;

    @GetMapping
    public List<EventShortDto> findEvents(
            @PathVariable long userId,
            @RequestParam(defaultValue = "0") @PositiveOrZero int from,
            @RequestParam(defaultValue = "10") @Positive int size
    ) {
        log.info("GET users/{}/events from={}, size={}", userId, from, size);

        return eventService.findAllEventsOfUser(userId, from, size);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto createEvent(@PathVariable long userId, @RequestBody @Valid NewEventDto dto) {
        log.info("POST users/{}/events with body {}", userId, dto);

        return eventService.createEvent(userId, dto);
    }

    @GetMapping("/{eventId}")
    public EventFullDto findEvent(@PathVariable long userId, @PathVariable long eventId) {
        log.info("GET users/{}/events/{}", userId, eventId);

        return eventService.findEvent(userId, eventId);

    }

    @PatchMapping("/{eventId}")
    public EventFullDto updateEvent(
            @PathVariable long userId,
            @PathVariable long eventId,
            @RequestBody @Valid UpdateEventUserRequest dto
    ) {
        log.info("PATCH users/{}/events/{}", userId, eventId);

        return eventService.updateEvent(userId, eventId, dto);
    }

    @GetMapping("/{eventId}/requests")
    public List<ParticipationRequestDto> findAllRequestsForEvent(@PathVariable long userId, @PathVariable long eventId) {
        log.info("GET users/{}/events/{}/requests", userId, eventId);

        return requestService.findAllRequestsForEvent(userId, eventId);
    }

    @PatchMapping("/{eventId}/requests")
    public EventRequestStatusUpdateResult updateStatusForEventRequests(
            @PathVariable long userId,
            @PathVariable long eventId,
            @RequestBody @Valid EventRequestStatusUpdateRequest statusUpdateDto
    ) {
        log.info("PATCH users/{}/events/{}/requests", userId, eventId);

        return requestService.updateStatus(userId, eventId, statusUpdateDto);
    }
}
