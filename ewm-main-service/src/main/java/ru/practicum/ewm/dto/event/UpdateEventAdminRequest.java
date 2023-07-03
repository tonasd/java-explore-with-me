package ru.practicum.ewm.dto.event;

import lombok.Data;

import javax.validation.constraints.AssertTrue;
import java.time.LocalDateTime;
import java.util.Objects;

@Data
public class UpdateEventAdminRequest extends UpdateEventRequest {
    private StateAction stateAction;

    public enum StateAction {
        PUBLISH_EVENT, REJECT_EVENT
    }

    @AssertTrue(message = "Event must start not earlier than one hour from now")
    private boolean isStartsFromNowAndLater() {
        return Objects.isNull(eventDate) || eventDate.isAfter(LocalDateTime.now().plusHours(1));
    }
}
