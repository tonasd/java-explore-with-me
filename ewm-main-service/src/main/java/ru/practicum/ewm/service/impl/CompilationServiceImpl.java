package ru.practicum.ewm.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.dto.compilation.CompilationDto;
import ru.practicum.ewm.dto.compilation.NewCompilationDto;
import ru.practicum.ewm.dto.compilation.UpdateCompilationRequest;
import ru.practicum.ewm.dto.event.EventShortDto;
import ru.practicum.ewm.exception.CompilationNotFoundException;
import ru.practicum.ewm.exception.EventNotFoundException;
import ru.practicum.ewm.mapper.CompilationMapper;
import ru.practicum.ewm.mapper.EventMapper;
import ru.practicum.ewm.model.Compilation;
import ru.practicum.ewm.model.Event;
import ru.practicum.ewm.repository.CompilationRepository;
import ru.practicum.ewm.repository.EventRepository;
import ru.practicum.ewm.service.CompilationService;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompilationServiceImpl implements CompilationService {
    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;
    private final EventServiceImpl eventService;
    @Override
    @Transactional
    public CompilationDto create(NewCompilationDto dto) {
        List<Event> events = eventRepository.findAllById(dto.getEvents());
        if (dto.getEvents().size() != events.size()) {
            List<Long> exists = events.parallelStream().map(Event::getId).collect(Collectors.toUnmodifiableList());
            dto.getEvents().removeAll(exists);
            long eventIdNotExists = dto.getEvents().stream().findAny().get();
            throw new EventNotFoundException(eventIdNotExists);
        }

        Compilation comp = compilationRepository.save(CompilationMapper.mapToCompilation(dto, new HashSet<>(events)));
        System.out.println(comp);
        CompilationDto compilationDto = CompilationMapper.mapToCompilationDto(comp, getEventShortDtos(comp));

        return compilationDto;
    }

    @Override
    @Transactional
    public void delete(long compId) {
        if (!compilationRepository.existsById(compId)) {
            throw new CompilationNotFoundException(compId);
        }

        compilationRepository.deleteById(compId);
    }

    @Override
    @Transactional
    public CompilationDto update(long compId, UpdateCompilationRequest dto) {
        Compilation comp = compilationRepository.findById(compId)
                .orElseThrow(() -> new CompilationNotFoundException(compId));

        if (dto.getPinned() != null) {
            comp.setPinned(dto.getPinned());
        }
        if (dto.getTitle() != null) {
            comp.setTitle(dto.getTitle());
        }
        if (Objects.nonNull(dto.getEvents()) && !dto.getEvents().isEmpty()) {
            List<Event> events = eventRepository.findAllById(dto.getEvents());
            comp.getEvents().clear();
            comp.getEvents().addAll(events);
        }

        return CompilationMapper.mapToCompilationDto(comp, getEventShortDtos(comp));
    }

    @Override
    @Transactional(readOnly = true)
    public List<CompilationDto> show(Boolean pinned, int from, int size) {
        Page<Compilation> comps;

        PageRequest page = PageRequest.of(from/size, size);
        if (Objects.nonNull(pinned)) {
            comps = compilationRepository.findAllByPinnedIs(pinned, page);
        } else {
            comps = compilationRepository.findAll(page);
        }

        return comps.stream()
                .map(comp -> CompilationMapper.mapToCompilationDto(comp, getEventShortDtos(comp)))
                .collect(Collectors.toUnmodifiableList());
    }

    @Override
    @Transactional(readOnly = true)
    public CompilationDto show(long compId) {
        Compilation comp = compilationRepository.findById(compId)
                .orElseThrow(() -> new CompilationNotFoundException(compId));

        return CompilationMapper.mapToCompilationDto(comp, getEventShortDtos(comp));
    }

    @Transactional(readOnly = true)
    private Set<EventShortDto> getEventShortDtos(Compilation comp) {
        Set<EventShortDto> eventShortDtoSet = comp.getEvents().stream()
                .map(event -> {
                    long confirmedRequests = eventService.getConfirmedRequests(event.getId());
                    long views = eventService.getViews(event.getId());
                    return EventMapper.mapToEventShortDto(event, confirmedRequests, views);
                })
                .collect(Collectors.toSet());
        return eventShortDtoSet;
    }
}
