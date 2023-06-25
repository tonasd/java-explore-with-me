package ru.practicum.ewm.service;

import ru.practicum.ewm.dto.event.ParticipationRequestDto;

import java.util.List;

public interface ParticipationRequestService {
    ParticipationRequestDto createRequest(long fromUserId, long forEventId);

    List<ParticipationRequestDto> findRequestsOfUser(long userId);

    ParticipationRequestDto cancelParticipationRequest(long userId, long requestId);
}
