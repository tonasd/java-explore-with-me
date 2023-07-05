package ru.practicum.ewm.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.dto.rating.RatingCreationDto;
import ru.practicum.ewm.exception.RulesViolationException;
import ru.practicum.ewm.model.Rating;
import ru.practicum.ewm.repository.ParticipationRequestRepository;
import ru.practicum.ewm.repository.RatingRepository;
import ru.practicum.ewm.repository.projection.RatingTopView;
import ru.practicum.ewm.service.RatingService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RatingServiceImpl implements RatingService {
    private final RatingRepository ratingRepository;
    private final ParticipationRequestRepository requestRepository;

    @Override
    public RatingCreationDto addRating(long participantId, long eventId, boolean isPositive) {

        // User must have attempted event  for that should be true the following
        // ParticipationRequest.requester.id == participantId and ParticipationRequest.event.id == eventId
        // ParticipationRequest.status = 'CONFIRMED'
        // ParticipationRequest.event.eventDate after now()
        boolean isParticipated = requestRepository.eventCanBeRated(participantId, eventId);
        if (!isParticipated) {
            throw new RulesViolationException("You must have participated the rated event");
        }

        Rating rating = new Rating(participantId, eventId, isPositive);

         rating = ratingRepository.save(rating);


        return new RatingCreationDto(rating.getParticipantId(), rating.getEventId(), rating.isPositive());
    }

    @Transactional(readOnly = true)
    @Override
    public List<RatingTopView> findTopEvents(int size) {
        List<RatingTopView> topEvents = ratingRepository.getTop(size);
        return topEvents;
    }
}
