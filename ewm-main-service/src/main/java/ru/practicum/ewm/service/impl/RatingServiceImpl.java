package ru.practicum.ewm.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import ru.practicum.ewm.dto.rating.RatingDto;
import ru.practicum.ewm.exception.EventNotFoundException;
import ru.practicum.ewm.exception.RulesViolationException;
import ru.practicum.ewm.exception.UserNotFoundException;
import ru.practicum.ewm.model.Event;
import ru.practicum.ewm.model.Rating;
import ru.practicum.ewm.model.User;
import ru.practicum.ewm.repository.EventRepository;
import ru.practicum.ewm.repository.ParticipationRequestRepository;
import ru.practicum.ewm.repository.RatingRepository;
import ru.practicum.ewm.repository.UserRepository;
import ru.practicum.ewm.service.RatingService;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class RatingServiceImpl implements RatingService {
    private final RatingRepository ratingRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final ParticipationRequestRepository requestRepository;

    @Override
    public RatingDto addRating(long participantId, long eventId, boolean isPositive) {

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

        return new RatingDto(rating.getParticipantId(), rating.getEventId(), rating.isPositive());
    }
}
