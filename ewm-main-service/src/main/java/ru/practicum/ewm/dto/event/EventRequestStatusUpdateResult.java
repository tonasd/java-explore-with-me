package ru.practicum.ewm.dto.event;

import lombok.Data;
import ru.practicum.ewm.model.ParticipationRequest;

import java.util.List;

@Data
public class EventRequestStatusUpdateResult {
    List<ParticipationRequestDto> confirmedRequests;
    List<ParticipationRequestDto> rejectedRequests;

}
