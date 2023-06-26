package ru.practicum.ewm.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.dto.event.EventRequestStatusUpdateRequest;
import ru.practicum.ewm.dto.event.EventRequestStatusUpdateResult;
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

@Slf4j
@Service
@RequiredArgsConstructor
public class ParticipationRequestServiceImpl implements ParticipationRequestService {
    private final ParticipationRequestRepository requestRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    @Override
    @Transactional
    public ParticipationRequestDto createRequest(long fromUserId, long forEventId) {
        User requester = userRepository.findById(fromUserId).orElseThrow(() -> new UserNotFoundException(fromUserId));
        Event event = getEvent(forEventId);

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
    @Transactional(readOnly = true)
    public List<ParticipationRequestDto> findRequestsOfUser(long userId) {
        checkUserExists(userId);

        Stream<ParticipationRequest> requestsStream = requestRepository.findAllByRequesterId(userId);

        return requestsStream.map(RequestMapper::map).collect(Collectors.toUnmodifiableList());
    }

    @Override
    @Transactional
    public ParticipationRequestDto cancelParticipationRequest(long requesterId, long requestId) {
        checkUserExists(requesterId);

        ParticipationRequest request = requestRepository.findByIdAndRequesterId(requestId, requesterId)
                .orElseThrow(() -> new RequestNotFoundException(requestId));
        request.setStatus(RequestStatus.CANCELED);
        requestRepository.save(request);

        return RequestMapper.map(request);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ParticipationRequestDto> findAllRequestsForEvent(long initiatorId, long eventId) {
        checkUserExists(initiatorId);
        Event event = getEvent(eventId);
        if (!event.getInitiator().getId().equals(initiatorId)) {
            log.info("Attempt to get requests for eventId={} from not initiator userId={}", event.getId(), initiatorId);
            throw new EventNotFoundException(eventId);
        }

        Stream<ParticipationRequest> requestsStream = requestRepository.findAllByEventId(eventId);

        return requestsStream.map(RequestMapper::map).collect(Collectors.toUnmodifiableList());
    }


    @Override
    @Transactional
    public EventRequestStatusUpdateResult updateStatus(
            long initiatorId,
            long eventId,
            EventRequestStatusUpdateRequest statusUpdateDto
    ) {
        checkUserExists(initiatorId);
        Event event = getEvent(eventId);
        if (!event.getInitiator().getId().equals(initiatorId)) {
            log.info("Attempt to update requests status for eventId={} from not initiator userId={}", event.getId(), initiatorId);
            throw new EventNotFoundException(eventId);
        }

        List<ParticipationRequest> requestList = requestRepository
                .findAllByEventIdAndIdIn(eventId, statusUpdateDto.getRequestIds());

        int participantLimit = event.getParticipantLimit();
        Long confirmedRequests = requestRepository.countByEventIdIsAndStatus(eventId, RequestStatus.CONFIRMED);
        // если для события лимит заявок равен 0 или отключена пре-модерация заявок, то подтверждение заявок не требуется
        if (event.isRequestModeration() || participantLimit != 0) {

            RequestStatus newStatus = RequestStatus.valueOf(statusUpdateDto.getStatus().name());
            for (ParticipationRequest request : requestList) {
                // статус можно изменить только у заявок, находящихся в состоянии ожидания (Ожидается код ошибки 409)
                if (!RequestStatus.PENDING.equals(request.getStatus())) {
                    throw new RulesViolationException(String.format(
                            "For requestId=%d status is not %s, you cannot change status",
                            request.getId(),
                            RequestStatus.PENDING)
                    );
                }
                // нельзя подтвердить заявку, если уже достигнут лимит по заявкам на данное событие (Ожидается код ошибки 409)
                if (confirmedRequests >= participantLimit) {
                    throw new RulesViolationException("Participants limit is exceeded");
                }

                request.setStatus(newStatus);
                if (RequestStatus.CONFIRMED.equals(newStatus)) confirmedRequests++;
            }
            requestRepository.saveAll(requestList);

            // если при подтверждении данной заявки, лимит заявок для события исчерпан, то все неподтверждённые заявки необходимо отклонить
            if (participantLimit != 0 && confirmedRequests >= participantLimit) {
                List<ParticipationRequest> requestsToBeRejected = requestRepository.findAllByEventIdAndStatusIs(eventId, RequestStatus.PENDING)
                        .peek(request -> request.setStatus(RequestStatus.REJECTED))
                        .collect(Collectors.toUnmodifiableList());
                requestRepository.saveAll(requestsToBeRejected);
                requestList.addAll(requestsToBeRejected);
            }
        }

        return RequestMapper.map(requestList);
    }

    @Transactional(propagation = Propagation.MANDATORY)
    private void checkUserExists(long userId) {
        if (!userRepository.existsById(userId)) {

            throw new UserNotFoundException(userId);
        }
    }

    @Transactional(propagation = Propagation.MANDATORY)
    private Event getEvent(long eventId) {
        return eventRepository.findById(eventId).orElseThrow(() -> new EventNotFoundException(eventId));
    }


}
