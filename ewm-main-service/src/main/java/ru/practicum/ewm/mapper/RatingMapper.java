package ru.practicum.ewm.mapper;

import ru.practicum.ewm.dto.rating.RatingDto;
import ru.practicum.ewm.repository.projection.EventRatingView;

public class RatingMapper {

    public static RatingDto mapToRatingDto(EventRatingView rating) {
        if (rating == null) return null;
        return new RatingDto(rating.getLikes(), rating.getDislikes());
    }
}
