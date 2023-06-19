package ru.practicum.stats.service;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.stats.service.model.HitRecord;
import ru.practicum.stats.service.model.HitView;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsRepository extends JpaRepository<HitRecord, Long> {

    @Query(value = "select new ru.practicum.HitView(h.app, h.uri, count(distinct h.ip) as hits) " +
            "from HitRecord h " +
            "where (h.created between :start and :end) " +
            "group by h.app, h.uri " +
            "order by hits desc")
    List<HitView> getStatsWithDistinctIps(LocalDateTime start, LocalDateTime end);

    @Query(value = "select new ru.practicum.HitView(h.app, h.uri, count(h.ip) as hits) " +
            "from HitRecord h " +
            "where (h.created between :start and :end) " +
            "group by h.app, h.uri " +
            "order by hits desc")
    List<HitView> getStats(LocalDateTime start, LocalDateTime end);
}
