package ru.practicum.ewm.mapper;

import ru.practicum.ewm.dto.user.NewUserRequest;
import ru.practicum.ewm.dto.user.UserDto;
import ru.practicum.ewm.dto.user.UserShortDto;
import ru.practicum.ewm.model.User;

public class UserMapper {
    public static User mapNewUserReuestToUser(NewUserRequest dto) {
        return new User(null, dto.getEmail(), dto.getName());
    }

    public static UserDto mapUserToUserDto(User user) {
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setEmail(user.getEmail());
        dto.setName(user.getName());

        return  dto;
    }

    public static UserShortDto mapUserToUserShortDto(User user) {
        UserShortDto dto = new UserShortDto();
        dto.setId(user.getId());
        dto.setName(user.getName());

        return  dto;
    }
}
