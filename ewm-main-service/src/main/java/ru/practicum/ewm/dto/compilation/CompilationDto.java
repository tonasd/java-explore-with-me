package ru.practicum.ewm.dto.compilation;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.practicum.ewm.dto.event.EventShortDto;

import java.util.Set;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CompilationDto {
    long id;
    boolean pinned;
    String title;
    Set<EventShortDto> events;
}
