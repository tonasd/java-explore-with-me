package ru.practicum.ewm.dto.event;

import lombok.Data;
import ru.practicum.ewm.model.Location;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Future;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.Objects;

@Data
public class UpdateEventAdminRequest {
    @Size(min = 20, max = 2000)
    String annotation;

    Integer category;

    @Size(min = 20, max = 7000)
    String description;

    @Future
    LocalDateTime eventDate;

    Boolean paid;

    Integer participantLimit;

    Boolean requestModeration;

    @Size(min = 3, max = 120)
    String title;

    StateAction stateAction;

    Location location;

    @AssertTrue(message = "Event must start not earlier than one hour from now")
    protected boolean isStartsFromNowAndLater() {
        return Objects.isNull(eventDate) || eventDate.isAfter(LocalDateTime.now().plusHours(1));
    }

    public enum StateAction {
        PUBLISH_EVENT, REJECT_EVENT
    }
}
