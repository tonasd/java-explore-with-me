package ru.practicum.ewm.dto.rating;

import lombok.Value;

@Value
public class RatingCreationDto {
    long participantId;
    long eventId;
    boolean isPositive;
}
