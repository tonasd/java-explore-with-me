package ru.practicum.ewm.repository.projection;

import ru.practicum.ewm.model.Category;
import ru.practicum.ewm.model.User;

import java.time.LocalDateTime;

public interface EventShortView {
    long getId();
    String getTitle();
    String getAnnotation();
    Category getCategory();
    LocalDateTime getEventDate();
    boolean isPaid();
    User getInitiator();
}
