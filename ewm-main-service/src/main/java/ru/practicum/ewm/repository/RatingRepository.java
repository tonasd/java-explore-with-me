package ru.practicum.ewm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.ewm.model.Rating;
import ru.practicum.ewm.repository.projection.EventRatingView;
import ru.practicum.ewm.repository.projection.RatingTopView;
import ru.practicum.ewm.repository.projection.UserRatingView;

import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

public interface RatingRepository extends JpaRepository<Rating, Rating.RatingId> {

    @Query(value = "SELECT SUM(CASE WHEN positive THEN 1 ELSE -1 END) " +
            "FROM events_rating " +
            "WHERE event_id = :eventId",
            nativeQuery = true)
    long countRating(long eventId);

    @Query(value = "SELECT event_id AS eventId, " +
            "count(event_id) FILTER (WHERE positive) AS likes, " +
            "count(event_id) FILTER (WHERE NOT positive) AS dislikes " +
            "FROM events_rating " +
            "WHERE event_id IN :eventsId " +
            "GROUP BY event_id",
            nativeQuery = true)
    Stream<EventRatingView> getRatingForEvents(Collection<Long> eventsId);

    @Query(value = "SELECT " +
            "e.id, e.event_date AS eventDate, e.title, " +
            "COUNT(er.event_id) FILTER (WHERE er.positive) AS likes, " +
            "COUNT(er.event_id) FILTER (WHERE NOT er.positive) AS dislikes " +
            "FROM events_rating AS er " +
            "LEFT JOIN events AS e ON er.event_id = e.id " +
            "GROUP BY er.event_id, e.id " +
            "ORDER BY (COUNT(er.event_id) FILTER (WHERE er.positive) - COUNT(er.event_id) FILTER (WHERE NOT er.positive)) DESC " +
            "LIMIT :size",
            nativeQuery = true)
    List<RatingTopView> getTop(int size);

    @Query(value = "SELECT e.initiator_id AS userId, " +
            "COUNT(e.initiator_id) FILTER (WHERE er.positive) AS likes, " +
            "COUNT(e.initiator_id) FILTER (WHERE NOT er.positive) AS dislikes " +
            "FROM events AS e " +
            "LEFT JOIN events_rating AS er ON e.id = er.event_id " +
            "WHERE e.initiator_id IN :usersId " +
            "GROUP BY e.initiator_id",
            nativeQuery = true)
    List<UserRatingView> getRatingForUsers(Collection<Long> usersId);
}
