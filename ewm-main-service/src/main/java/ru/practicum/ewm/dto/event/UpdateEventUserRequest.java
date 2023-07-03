package ru.practicum.ewm.dto.event;

import lombok.Data;

import javax.validation.constraints.AssertTrue;
import java.time.LocalDateTime;
import java.util.Objects;

@Data
public class UpdateEventUserRequest extends UpdateEventRequest {
    private StateAction stateAction;

    public enum StateAction {
        SEND_TO_REVIEW, CANCEL_REVIEW
    }

    @AssertTrue(message = "Event must start not earlier than two hours from now")
    private boolean isStartsFromNowAndLater() {
        return Objects.isNull(eventDate) || eventDate.isAfter(LocalDateTime.now().plusHours(2));
    }
}
