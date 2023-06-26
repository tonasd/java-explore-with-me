package ru.practicum.ewm.dto.event;

import lombok.Data;

import java.util.List;

@Data
public class EventRequestStatusUpdateRequest {
    List<Long> requestIds;
    Status status;

    public enum Status {
        CONFIRMED, REJECTED
    }
}
