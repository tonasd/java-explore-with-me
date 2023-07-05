package ru.practicum.ewm.repository.projection;

import java.time.LocalDateTime;

public interface RatingTopView {
    Long getId();

    String getTitle();

    LocalDateTime getEventDate();

    Long getLikes();

    Long getDislikes();
}
