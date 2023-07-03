package ru.practicum.ewm.controller.reg;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.dto.rating.RatingDto;
import ru.practicum.ewm.service.RatingService;

@Slf4j
@RestController
@RequestMapping("/users/{userId}")
@RequiredArgsConstructor
public class RatingController {
    private final RatingService service;

    @PutMapping("/like/{eventId}")
    public RatingDto like(
            @PathVariable long userId,
            @PathVariable long eventId
    ) {
        log.info("POST /users/{}/like/{}", userId, eventId);

        return service.addRating(userId, eventId, true);
    }

    @PutMapping("/dislike/{eventId}")
    public RatingDto dislike(
            @PathVariable long userId,
            @PathVariable long eventId
    ) {
        log.info("POST /users/{}/dislike/{}", userId, eventId);

        return service.addRating(userId, eventId, false);
    }
}
