package ru.practicum.ewm.mapper;

import ru.practicum.ewm.dto.rating.RatingDto;
import ru.practicum.ewm.repository.projection.RatingView;

public class RatingMapper {

    public static RatingDto mapToRatingDto(RatingView rating) {
        return new RatingDto(rating.getLikes(), rating.getDislikes());
    }
}
