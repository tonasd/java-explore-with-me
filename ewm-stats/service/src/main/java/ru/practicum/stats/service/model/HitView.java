package ru.practicum.stats.service.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.net.URI;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class HitView {
    String app;
    URI uri;
    long hits;

    public HitView(String app, String uri, long hits) {
        this.app = app;
        this.uri = URI.create(uri);
        this.hits = hits;
    }
}