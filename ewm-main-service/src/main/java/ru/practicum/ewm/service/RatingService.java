package ru.practicum.ewm.service;

import ru.practicum.ewm.dto.rating.RatingDto;

public interface RatingService {
    RatingDto addRating(long userId, long eventId, boolean isPositive);
}
