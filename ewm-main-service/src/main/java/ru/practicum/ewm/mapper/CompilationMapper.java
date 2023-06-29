package ru.practicum.ewm.mapper;

import ru.practicum.ewm.dto.compilation.CompilationDto;
import ru.practicum.ewm.dto.compilation.NewCompilationDto;
import ru.practicum.ewm.dto.event.EventShortDto;
import ru.practicum.ewm.model.Compilation;
import ru.practicum.ewm.model.Event;

import java.util.Set;

public class CompilationMapper {
    public static Compilation mapToCompilation(NewCompilationDto dto, Set<Event> events) {
        Compilation compilation = new Compilation();
        compilation.setPinned(dto.isPinned());
        compilation.setTitle(dto.getTitle());
        compilation.setEvents(events);

        return compilation;
    }

    public static CompilationDto mapToCompilationDto(Compilation compilation, Set<EventShortDto> eventShortDtos) {
        CompilationDto dto = new CompilationDto();
        dto.setId(compilation.getId());
        dto.setPinned(compilation.isPinned());
        dto.setTitle(compilation.getTitle());
        dto.setEvents(eventShortDtos);

        return dto;
    }
}
