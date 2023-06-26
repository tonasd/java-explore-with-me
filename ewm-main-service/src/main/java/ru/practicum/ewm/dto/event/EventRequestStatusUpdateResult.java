package ru.practicum.ewm.dto.event;

import lombok.Data;

import java.util.LinkedList;
import java.util.List;

@Data
public class EventRequestStatusUpdateResult {
    List<ParticipationRequestDto> confirmedRequests = new LinkedList<>();
    List<ParticipationRequestDto> rejectedRequests = new LinkedList<>();
}
