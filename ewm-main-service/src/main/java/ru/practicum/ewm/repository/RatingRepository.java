package ru.practicum.ewm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewm.model.Rating;

public interface RatingRepository extends JpaRepository<Rating, Rating.RatingId> {
}
