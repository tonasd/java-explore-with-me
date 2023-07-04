package ru.practicum.ewm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.ewm.model.ParticipationRequest;
import ru.practicum.ewm.model.RequestStatus;
import ru.practicum.ewm.repository.projection.RequestView;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public interface ParticipationRequestRepository extends JpaRepository<ParticipationRequest, Long> {

    Stream<ParticipationRequest> findAllByRequesterId(long userId);

    Optional<ParticipationRequest> findByIdAndRequesterId(long requestId, long requesterId);

    Stream<ParticipationRequest> findAllByEventId(long eventId);

    List<ParticipationRequest> findAllByEventIdAndIdIn(long eventId, List<Long> requestIds);

    long countByEventIdIsAndStatus(long eventId, RequestStatus requestStatus);

    @Query(value = "SELECT pr.event_id AS eventId, count(pr.id) as count " +
            "FROM requests AS pr " +
            "WHERE pr.event_id IN :eventsId AND pr.status = :#{#status.name()} " +
            "GROUP BY pr.event_id",
            nativeQuery = true)
    List<RequestView> countRequests(Collection<Long> eventsId, @Param("status") RequestStatus requestStatus);

    Stream<ParticipationRequest> findAllByEventIdAndStatusIs(long eventId, RequestStatus requestStatus);

    @Query(value = "SELECT count(*) > 0 " +
            "FROM ParticipationRequest AS pr " +
            "WHERE pr.requester.id = :participantId " +
            "AND pr.event.id = :eventId " +
            "AND pr.status = 'CONFIRMED' " +
            "AND pr.event.eventDate < CURRENT_TIMESTAMP")
    boolean eventCanBeRated(long participantId, long eventId);
}
