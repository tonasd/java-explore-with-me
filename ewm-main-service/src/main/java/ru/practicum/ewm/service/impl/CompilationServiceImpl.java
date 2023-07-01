package ru.practicum.ewm.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
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
import ru.practicum.ewm.model.RequestStatus;
import ru.practicum.ewm.repository.CompilationRepository;
import ru.practicum.ewm.repository.EventRepository;
import ru.practicum.ewm.repository.ParticipationRequestRepository;
import ru.practicum.ewm.repository.projection.RequestView;
import ru.practicum.ewm.service.CompilationService;
import ru.practicum.ewm.stats.Stats;
import ru.practicum.ewm.stats.ViewShortDto;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompilationServiceImpl implements CompilationService {
    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;
    private final ParticipationRequestRepository requestRepository;
    private final Stats stats;

    @Override
    @Transactional
    public CompilationDto create(NewCompilationDto dto) {
        List<Event> events = eventRepository.findAllById(dto.getEvents());
        if (dto.getEvents().size() != events.size()) {
            List<Long> exists = events.parallelStream().map(Event::getId).collect(Collectors.toUnmodifiableList());
            dto.getEvents().removeAll(exists);
            long eventIdNotExists = dto.getEvents().stream().findAny().get();
            throw new EventNotFoundException(dto.getEvents());
        }

        Compilation comp = compilationRepository.save(CompilationMapper.mapToCompilation(dto, new HashSet<>(events)));
        System.out.println(comp);

        return CompilationMapper.mapToCompilationDto(comp, getEventShortDtos(comp));
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
        if (dto.getTitle() != null && !dto.getTitle().isBlank()) {
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
        Slice<Compilation> comps;

        PageRequest page = PageRequest.of(from / size, size);
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
        //key = event id, value = counted confirmed requests
        Map<Long, Long> confirmedRequests = requestRepository.countRequests(
                        comp.getEvents().stream().map(Event::getId).collect(Collectors.toUnmodifiableList()),
                        RequestStatus.CONFIRMED).stream()
                .collect(Collectors.toMap(RequestView::getEventId, RequestView::getCount));
        //key = event id, value = counted views
        Map<Long, Long> views = stats.getViewsForEvents(comp.getEvents()).stream()
                .collect(Collectors.toMap(ViewShortDto::getEventId, ViewShortDto::getViews));

        Set<EventShortDto> eventShortDtoSet = comp.getEvents().stream()
                .map(event -> {
                    long eventId = event.getId();
                    return EventMapper.mapToEventShortDto(event,
                            confirmedRequests.getOrDefault(eventId, 0L),
                            views.getOrDefault(eventId, 0L));
                })
                .collect(Collectors.toSet());
        return eventShortDtoSet;
    }
}
