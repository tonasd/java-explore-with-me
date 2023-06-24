package ru.practicum.ewm.dto.event;

import lombok.Builder;
import lombok.Data;
import ru.practicum.ewm.dto.user.UserShortDto;
import ru.practicum.ewm.model.EventState;

import java.time.LocalDateTime;

@Data
@Builder
public class EventShortDto {
    long id;
    String title;
    String annotation;
    CategoryDto category;
    LocalDateTime eventDate;
    boolean paid;
    UserShortDto initiator;
    long confirmedRequests;
    long views;
}
