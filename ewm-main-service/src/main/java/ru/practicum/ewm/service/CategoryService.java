package ru.practicum.ewm.service;

import ru.practicum.ewm.dto.event.CategoryDto;
import ru.practicum.ewm.dto.event.NewCategoryDto;

import java.util.List;

public interface CategoryService {
    CategoryDto create(NewCategoryDto category);

    void delete(int catId);

    CategoryDto updateName(int catId, String newName);

    List<CategoryDto> findAll(int from, int size);

    CategoryDto findOne(int catId);
}
