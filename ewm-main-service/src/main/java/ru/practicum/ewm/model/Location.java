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
    private Float lat;

    @NotNull
    private Float lon;
}
