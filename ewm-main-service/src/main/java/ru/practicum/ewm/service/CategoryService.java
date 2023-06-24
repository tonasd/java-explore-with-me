package ru.practicum.ewm.service;

import ru.practicum.ewm.dto.event.CategoryDto;
import ru.practicum.ewm.dto.event.NewCategoryDto;

public interface CategoryService {
    CategoryDto create(NewCategoryDto category);

    void delete(int catId);

    CategoryDto updateName(int catId, String newName);
}
