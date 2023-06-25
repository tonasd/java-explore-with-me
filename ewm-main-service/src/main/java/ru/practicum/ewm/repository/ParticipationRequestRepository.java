package ru.practicum.ewm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewm.model.ParticipationRequest;
import ru.practicum.ewm.model.RequestStatus;

import java.util.Optional;
import java.util.stream.Stream;

public interface ParticipationRequestRepository extends JpaRepository<ParticipationRequest, Long> {
    long countByEventIdIsAndStatusNot(long eventId, RequestStatus state);

    Stream<ParticipationRequest> findAllByRequesterId(long userId);

    Optional<ParticipationRequest> findByIdAndRequesterId(long requestId, long requesterId);
}
