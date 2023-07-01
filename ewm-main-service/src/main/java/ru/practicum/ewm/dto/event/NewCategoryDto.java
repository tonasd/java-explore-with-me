package ru.practicum.ewm.dto.event;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NewCategoryDto {
    @NotBlank
    @Size(max = 50)
    String name;
}
