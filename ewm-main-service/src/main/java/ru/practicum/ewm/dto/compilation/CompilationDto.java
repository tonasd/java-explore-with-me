package ru.practicum.ewm.dto.compilation;

import lombok.Data;
import ru.practicum.ewm.dto.event.EventShortDto;

import java.util.Set;

@Data
public class CompilationDto {
    long id;
    boolean pinned;
    String title;
    Set<EventShortDto> events;
}
