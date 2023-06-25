package ru.practicum.ewm.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.dto.event.ParticipationRequestDto;
import ru.practicum.ewm.exception.EventNotFoundException;
import ru.practicum.ewm.exception.RequestNotFoundException;
import ru.practicum.ewm.exception.RulesViolationException;
import ru.practicum.ewm.exception.UserNotFoundException;
import ru.practicum.ewm.mapper.RequestMapper;
import ru.practicum.ewm.model.*;
import ru.practicum.ewm.repository.EventRepository;
import ru.practicum.ewm.repository.ParticipationRequestRepository;
import ru.practicum.ewm.repository.UserRepository;
import ru.practicum.ewm.service.ParticipationRequestService;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class ParticipationRequestServiceImpl implements ParticipationRequestService {
    private final ParticipationRequestRepository requestRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    @Override
    public ParticipationRequestDto createRequest(long fromUserId, long forEventId) {
        User requester = userRepository.findById(fromUserId).orElseThrow(() -> new UserNotFoundException(fromUserId));
        Event event = eventRepository.findById(forEventId).orElseThrow(() -> new EventNotFoundException(forEventId));

        // инициатор события не может добавить запрос на участие в своём событии (Ожидается код ошибки 409)
        if (event.getInitiator().getId().equals(fromUserId)) {
            throw new RulesViolationException("Initiator cannot request for participation in own event");
        }
        // нельзя участвовать в неопубликованном событии (Ожидается код ошибки 409)
        if (!event.getState().equals(EventState.PUBLISHED)) {
            throw new RulesViolationException("Event not published yet. " +
                    "Please request for participation only in published events");
        }
        // если у события достигнут лимит запросов на участие - необходимо вернуть ошибку (Ожидается код ошибки 409)
        long requestsCount = requestRepository.countByEventIdIsAndStatusNot(forEventId, RequestStatus.CANCELED);
        if (event.getParticipantLimit() != 0 && requestsCount >= event.getParticipantLimit()) {
            throw new RulesViolationException("Number of participants for the event has reached maximum");
        }

        ParticipationRequest request = requestRepository.save(RequestMapper.map(requester, event));
        return RequestMapper.map(request);
    }

    @Override
    public List<ParticipationRequestDto> findRequestsOfUser(long userId) {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException(userId);
        }

        Stream<ParticipationRequest> requestsStream = requestRepository.findAllByRequesterId(userId);

        return requestsStream.map(RequestMapper::map).collect(Collectors.toUnmodifiableList());
    }

    @Override
    public ParticipationRequestDto cancelParticipationRequest(long requesterId, long requestId) {
        if (!userRepository.existsById(requesterId)) {
            throw new UserNotFoundException(requesterId);
        }

        ParticipationRequest request = requestRepository.findByIdAndRequesterId(requestId, requesterId)
                .orElseThrow(() -> new RequestNotFoundException(requestId));
        request.setStatus(RequestStatus.CANCELED);

        return RequestMapper.map(request);
    }


}
