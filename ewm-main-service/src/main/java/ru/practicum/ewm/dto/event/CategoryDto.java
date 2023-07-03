package ru.practicum.ewm.dto.event;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Setter
@Getter
@EqualsAndHashCode
@ToString
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CategoryDto {
    int id;
    String name;
}
