package ru.practicum.ewm.repository.projection;

public interface RatingView {
    long getEventId();
    long getLikes();
    long getDislikes();
}
