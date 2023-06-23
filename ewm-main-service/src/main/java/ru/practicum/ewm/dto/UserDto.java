package ru.practicum.ewm.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
/**
 * Dto for main purposes
 */
public class UserDto {
long id;
String email;
String name;
}
