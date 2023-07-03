package ru.practicum.ewm.dto.rating;

import lombok.Value;

@Value
public class RatingDto {
    long participantId;
    long eventId;
    boolean isPositive;
}
