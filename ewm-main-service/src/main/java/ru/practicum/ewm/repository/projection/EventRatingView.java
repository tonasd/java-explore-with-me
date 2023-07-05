package ru.practicum.ewm.repository.projection;

public interface EventRatingView {
    long getEventId();

    long getLikes();

    long getDislikes();
}
