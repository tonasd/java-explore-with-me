package ru.practicum.ewm.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "events_rating")
@IdClass(Rating.RatingId.class)
@Setter
@Getter
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
public class Rating {
    @Id
    @Column(name = "participant_id", nullable = false)
    long participantId;

    @Id
    @Column(name = "event_id", nullable = false)
    long eventId;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "participant_id", insertable=false, updatable=false)
    @ToString.Exclude
    transient User participant;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", insertable = false, updatable = false)
    @ToString.Exclude
    transient Event event;

    @Column(name = "positive")
    boolean isPositive;

    public Rating(Long participantId, Long eventId, boolean isPositive) {
        this.participantId = participantId;
        this.eventId = eventId;
        this.isPositive = isPositive;
    }

    @EqualsAndHashCode
    @Getter @Setter
    @ToString
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RatingId implements Serializable {
        long participantId;
        long eventId;
    }
}
