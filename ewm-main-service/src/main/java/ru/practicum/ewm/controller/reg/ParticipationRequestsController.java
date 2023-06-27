package ru.practicum.ewm.controller.reg;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.dto.event.ParticipationRequestDto;
import ru.practicum.ewm.service.ParticipationRequestService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users/{userId}/requests")
@RequiredArgsConstructor
public class ParticipationRequestsController {
    private final ParticipationRequestService service;
    private static final String API_PREFIX = "/users/{userId}/requests";

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ParticipationRequestDto participationRequest(
            @PathVariable long userId,
            @RequestParam long eventId
    ) {
        log.info("POST /users/{}/requests for eventId={}", userId, eventId);
        return service.createRequest(userId, eventId);
    }

    @GetMapping
    public List<ParticipationRequestDto> findUserParticipationRequests(@PathVariable long userId) {
        log.info("GET /users/{}/requests", userId);
        return service.findRequestsOfUser(userId);
    }

    @PatchMapping("/{requestId}/cancel")
    public ParticipationRequestDto cancelParticipationRequest(@PathVariable long userId, @PathVariable long requestId) {
        log.info("PATCH /users/{}/requests/{}/cancel", userId, requestId);
        return service.cancelParticipationRequest(userId, requestId);
    }
}
