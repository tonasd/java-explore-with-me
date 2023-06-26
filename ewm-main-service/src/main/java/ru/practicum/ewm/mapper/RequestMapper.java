package ru.practicum.ewm.mapper;

import ru.practicum.ewm.dto.event.EventRequestStatusUpdateResult;
import ru.practicum.ewm.dto.event.ParticipationRequestDto;
import ru.practicum.ewm.model.Event;
import ru.practicum.ewm.model.ParticipationRequest;
import ru.practicum.ewm.model.RequestStatus;
import ru.practicum.ewm.model.User;

import java.time.LocalDateTime;
import java.util.List;

public class RequestMapper {
    public static ParticipationRequestDto map(ParticipationRequest request) {
        return new ParticipationRequestDto(
                request.getId(),
                request.getEvent().getId(),
                request.getRequester().getId(),
                request.getStatus(),
                request.getCreated()
        );
    }

    public static ParticipationRequest map(User requester, Event event) {
        ParticipationRequest request = new ParticipationRequest();
        request.setRequester(requester);
        request.setEvent(event);
        request.setCreated(LocalDateTime.now());
        if (event.isRequestModeration()) {
            request.setStatus(RequestStatus.PENDING);
        } else {
            request.setStatus(RequestStatus.CONFIRMED);
        }

        return request;
    }

    public static EventRequestStatusUpdateResult map(List<ParticipationRequest> requestList) {
        EventRequestStatusUpdateResult result = new EventRequestStatusUpdateResult();
        for (ParticipationRequest request : requestList) {
            RequestStatus status = request.getStatus();
            if (RequestStatus.CONFIRMED.equals(status)) {
                result.getConfirmedRequests().add(map(request));
            } else if (RequestStatus.REJECTED.equals(status)) {
                result.getRejectedRequests().add(map(request));
            }
        }

        return result;
    }

}
