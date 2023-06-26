package ru.practicum.ewm.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewm.model.Event;
import ru.practicum.ewm.repository.projection.EventShortView;

import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {

    List<EventShortView> findAllByInitiatorId(long userId, Pageable page);

}
