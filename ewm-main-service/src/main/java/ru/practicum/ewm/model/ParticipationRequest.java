package ru.practicum.ewm.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "requests", uniqueConstraints = @UniqueConstraint(name = "uq_requester_event", columnNames = {"event_id", "requester_id"}))
@Setter
@Getter
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class ParticipationRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @ToString.Exclude
    Event event;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @ToString.Exclude
    User requester;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(10) default 'PENDING'")
    RequestStatus status;

    @Column(nullable = false)
    LocalDateTime created;
}
