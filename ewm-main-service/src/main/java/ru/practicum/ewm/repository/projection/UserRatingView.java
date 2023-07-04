package ru.practicum.ewm.repository.projection;

public interface UserRatingView {
    long getUserId();
    long getLikes();
    long getDislikes();
}
