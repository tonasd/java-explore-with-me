package ru.practicum.ewm.dto.event;

import lombok.Data;
import ru.practicum.ewm.model.Location;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.Objects;

@Data
public class NewEventDto {
    @NotNull
    @Size(min = 3, max = 120)
    String title;

    @NotNull
    @Size(min = 20, max = 2000)
    String annotation;

    @NotNull
    Integer category;

    @NotNull
    @Size(min = 20, max = 7000)
    String description;

    @NotNull
    LocalDateTime eventDate;

    boolean paid = false;

    int participantLimit = 0;

    boolean requestModeration = true;

    @NotNull
    Location location;


    @AssertTrue(message = "Event must start not earlier than two hours from now")
    private boolean isStarts2HoursFromNowAndLater() {
        return Objects.isNull(eventDate) || eventDate.isAfter(LocalDateTime.now().plusHours(2));
    }

}
