package ru.practicum.ewm.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.awt.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "events")
@Setter
@Getter
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false, length = 120)
    String title;

    @Column(nullable = false, length = 2000)
    String annotation;

    @ManyToOne(optional = false)
    Category category;

    @Column(name = "created_on" ,nullable = false)
    LocalDateTime createdOn;

    @Column(nullable = false, length = 7000)
    String description;

    @Column(name = "event_date", nullable = false)
    LocalDateTime eventDate;

    @ManyToOne(optional = false)
    User initiator;

    boolean paid;

    @Column(name = "participant_limit", columnDefinition = "int default 0")
    int participantLimit;

    @Column(name = "request_moderation", columnDefinition = "boolean default true")
    boolean requestModeration;

    @Enumerated(value = EnumType.STRING)
    @Column(columnDefinition = "varchar(10) default 'PENDING'")
    EventState state;

    @Column(name = "published_on", nullable = true)
    LocalDateTime publishedOn;

    @Column(name = "location_lat", nullable = false)
    Float locationLat;

    @Column(name = "location_lon", nullable = false)
    Float locationLon;
}
