package ru.practicum.stats.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.stats.dto.CreationDto;
import ru.practicum.stats.dto.ViewDto;
import ru.practicum.stats.service.model.HitView;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class StatsService {
    private final StatsRepository repository;

    @Transactional
    public void save(CreationDto dto) {
        repository.save(StatsMapper.mapCreationDtoToHitRecord(dto));
    }

    public List<ViewDto> find(LocalDateTime start, LocalDateTime end, List<URI> uris, boolean unique) {
        List<HitView> result;

        if (unique) {
            result = repository.getStatsWithDistinctIps(start, end);
        } else {
            result = repository.getStats(start, end);
        }

        if (uris != null && !uris.isEmpty()) {
            result = result.stream()
                    .filter(hitView -> uris.contains(hitView.getUri()))
                    .collect(Collectors.toUnmodifiableList());
        }

        return result.stream().map(StatsMapper::mapHitViewToViewDto).collect(Collectors.toUnmodifiableList());
    }
}
