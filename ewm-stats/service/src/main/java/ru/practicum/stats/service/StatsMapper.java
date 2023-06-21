package ru.practicum.stats.service;

import ru.practicum.stats.dto.CreationDto;
import ru.practicum.stats.dto.ViewDto;
import ru.practicum.stats.service.model.HitRecord;
import ru.practicum.stats.service.model.HitView;

public class StatsMapper {

    public static HitRecord mapCreationDtoToHitRecord(CreationDto dto) {
        HitRecord record = new HitRecord();
        record.setIp(dto.getIp().getHostAddress());
        record.setCreated(dto.getTimestamp());
        record.setApp(dto.getApp());
        record.setUri(dto.getUri().getPath());

        return record;
    }

    public static ViewDto mapHitViewToViewDto(HitView hitView) {
        return new ViewDto(
                hitView.getApp(),
                hitView.getUri(),
                hitView.getHits()
        );
    }
}
