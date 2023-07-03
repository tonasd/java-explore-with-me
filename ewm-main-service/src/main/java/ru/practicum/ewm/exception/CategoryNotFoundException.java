package ru.practicum.ewm.exception;

public class CategoryNotFoundException extends NotFoundException {
    public CategoryNotFoundException(int catId) {
        super(String.format("Category with id=%d was not found", catId));
    }
}
