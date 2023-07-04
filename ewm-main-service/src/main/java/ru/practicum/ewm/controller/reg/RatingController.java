package ru.practicum.ewm.controller.reg;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.ewm.dto.rating.RatingCreationDto;
import ru.practicum.ewm.service.RatingService;

@Slf4j
@RestController
@RequestMapping("/users/{userId}")
@RequiredArgsConstructor
public class RatingController {
    private final RatingService service;

    @PutMapping("/like/{eventId}")
    public RatingCreationDto like(
            @PathVariable long userId,
            @PathVariable long eventId
    ) {
        log.info("POST /users/{}/like/{}", userId, eventId);

        return service.addRating(userId, eventId, true);
    }

    @PutMapping("/dislike/{eventId}")
    public RatingCreationDto dislike(
            @PathVariable long userId,
            @PathVariable long eventId
    ) {
        log.info("POST /users/{}/dislike/{}", userId, eventId);

        return service.addRating(userId, eventId, false);
    }
}
