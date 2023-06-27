package ru.practicum.ewm.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.dto.event.CategoryDto;
import ru.practicum.ewm.dto.event.NewCategoryDto;
import ru.practicum.ewm.exception.CategoryNotFoundException;
import ru.practicum.ewm.mapper.CategoryMapper;
import ru.practicum.ewm.model.Category;
import ru.practicum.ewm.repository.CategoryRepository;
import ru.practicum.ewm.service.CategoryService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository repository;

    @Override
    public CategoryDto create(NewCategoryDto dto) {
        Category category = repository.save(CategoryMapper.mapToCategory(dto));
        return CategoryMapper.mapToCategoryDto(category);
    }

    @Override
    @Transactional
    public void delete(int catId) {
        if (!repository.existsById(catId)) {
            throw new CategoryNotFoundException(catId);
        }
        // TODO: to be implemented Обратите внимание: с категорией не должно быть связано ни одного события.

        repository.deleteById(catId);
    }

    @Override
    @Transactional
    public CategoryDto updateName(int catId, String newName) {
        Category category = repository.findById(catId).orElseThrow(() -> new CategoryNotFoundException(catId));
        category.setName(newName);
        category = repository.save(category);
        return CategoryMapper.mapToCategoryDto(category);
    }

    @Override
    public List<CategoryDto> findAll(int from, int size) {

        PageRequest page = PageRequest.of(from / size, size);

        return repository.findAll(page).get()
                .map(CategoryMapper::mapToCategoryDto)
                .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public CategoryDto findOne(int catId) {
        Category category = repository.findById(catId)
                .orElseThrow(() -> new CategoryNotFoundException(catId));
        return CategoryMapper.mapToCategoryDto(category);
    }


}
