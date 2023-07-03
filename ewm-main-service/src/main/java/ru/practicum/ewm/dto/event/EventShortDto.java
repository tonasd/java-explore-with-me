package ru.practicum.ewm.dto.event;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.practicum.ewm.dto.user.UserShortDto;

import java.time.LocalDateTime;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
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
