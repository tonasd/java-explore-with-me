package ru.practicum.ewm.service;

import ru.practicum.ewm.dto.rating.RatingCreationDto;
import ru.practicum.ewm.repository.projection.RatingTopView;

import java.util.List;

public interface RatingService {
    RatingCreationDto addRating(long userId, long eventId, boolean isPositive);

    List<RatingTopView> findTopEvents(int size);
}
