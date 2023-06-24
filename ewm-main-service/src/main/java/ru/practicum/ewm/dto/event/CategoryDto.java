package ru.practicum.ewm.dto.event;

import lombok.*;

@Setter
@Getter
@EqualsAndHashCode
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDto {
    int id;
    String name;
}
