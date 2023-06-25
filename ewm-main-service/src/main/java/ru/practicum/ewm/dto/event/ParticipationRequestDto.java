package ru.practicum.ewm.dto.event;

import lombok.*;
import ru.practicum.ewm.model.RequestStatus;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ParticipationRequestDto {
    long id;
    long event;
    long requester;
    RequestStatus status;
    LocalDateTime created;
}
