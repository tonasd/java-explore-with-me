package ru.practicum.ewm.mapper;

import ru.practicum.ewm.dto.NewUserRequest;
import ru.practicum.ewm.dto.UserDto;
import ru.practicum.ewm.model.User;

public class UserMapper {
    public static User mapNewUserReuestToUser(NewUserRequest dto) {
        return new User(null, dto.getEmail(), dto.getName());
    }

    public static UserDto mapUserToUserDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setEmail(user.getEmail());
        userDto.setName(user.getName());

        return  userDto;
    }
}
