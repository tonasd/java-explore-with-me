package ru.practicum.ewm.exception;

public class UserNotFoundException extends NotFoundException {
    public UserNotFoundException(long userId) {
        super(String.format("User with id=%d was not found", userId));
    }
}
