package ru.practicum.ewm.model;

import lombok.*;

import javax.validation.constraints.NotNull;

@Setter
@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class Location {
    @NotNull
    Float lat;

    @NotNull
    Float lon;
}
