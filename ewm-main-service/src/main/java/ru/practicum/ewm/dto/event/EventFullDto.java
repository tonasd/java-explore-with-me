package ru.practicum.ewm.dto.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.ewm.dto.user.UserShortDto;
import ru.practicum.ewm.model.EventState;
import ru.practicum.ewm.model.Location;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventFullDto {
    long id;
    String title;
    String annotation;
    CategoryDto category;
    String description;
    LocalDateTime eventDate;
    boolean paid;
    int participantLimit;
    boolean requestModeration;
    long confirmedRequests;
    LocalDateTime createdOn;
    UserShortDto initiator;
    LocalDateTime publishedOn;
    EventState state;
    Location location;
    long views;
}
