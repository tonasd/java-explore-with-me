package ru.practicum.ewm.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "compilations")
@Setter
@Getter
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class Compilation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false, columnDefinition = "boolean default false")
    boolean pinned;

    @Column(nullable = false, length = 50)
    String title;

    @ToString.Exclude
    @ManyToMany(cascade = CascadeType.PERSIST,
            targetEntity = Event.class)
    @JoinTable(name = "compilation_event",
            joinColumns = @JoinColumn(name = "compilation_id", referencedColumnName = "id", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "event_id", referencedColumnName = "id", nullable = false))
    Set<Event> events = new HashSet<>();
}
