package ru.practicum.stats.service.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "hits")
@Getter
@Setter
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class HitRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    @Column(name = "app_name", nullable = false, length = 64)
    String app;

    @Column(name = "request_uri", nullable = false)
    String uri;

    @Column(name = "requester_ip", nullable = false)
    String ip;

    @Column(name = "request_date", nullable = false)
    LocalDateTime created;
}
