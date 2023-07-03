package ru.practicum.ewm.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.ewm.dto.event.NewCategoryDto;
import ru.practicum.ewm.model.Category;

@NoArgsConstructor(access = AccessLevel.PRIVATE)  // @UtilityClass as another version to restrict creation
public class CategoryMapper {
    public static ru.practicum.ewm.dto.event.CategoryDto mapToCategoryDto(Category category) {
        return new ru.practicum.ewm.dto.event.CategoryDto(category.getId(), category.getName());
    }

    public static Category mapToCategory(NewCategoryDto dto) {
        return new Category(null, dto.getName());
    }

    public static Category mapToCategory(ru.practicum.ewm.dto.event.CategoryDto dto) {
        return new Category(dto.getId(), dto.getName());
    }
}
