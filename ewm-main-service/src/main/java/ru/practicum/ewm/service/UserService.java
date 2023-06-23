package ru.practicum.ewm.service;

import ru.practicum.ewm.dto.NewUserRequest;
import ru.practicum.ewm.dto.UserDto;

import java.util.List;

public interface UserService {
    List<UserDto> find(List<Long> ids, int from, int size);

    UserDto create(NewUserRequest dto);

    void delete(long userId);
}
