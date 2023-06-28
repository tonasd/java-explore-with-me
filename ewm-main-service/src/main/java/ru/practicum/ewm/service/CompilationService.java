package ru.practicum.ewm.service;

import ru.practicum.ewm.dto.compilation.CompilationDto;
import ru.practicum.ewm.dto.compilation.NewCompilationDto;
import ru.practicum.ewm.dto.compilation.UpdateCompilationRequest;

import java.util.List;

public interface CompilationService {
    CompilationDto create(NewCompilationDto dto);

    void delete(long compId);

    CompilationDto update(long compId, UpdateCompilationRequest dto);

    List<CompilationDto> show(Boolean pinned, int from, int size);

    CompilationDto show(long compId);
}
