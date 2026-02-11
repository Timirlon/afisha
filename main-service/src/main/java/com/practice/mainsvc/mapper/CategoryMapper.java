package com.practice.mainsvc.mapper;

import com.practice.mainsvc.dto.category.CategoryDto;
import com.practice.mainsvc.dto.category.NewCategoryDto;
import com.practice.mainsvc.model.Category;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CategoryMapper {
    public CategoryDto toDto(Category category) {
        return CategoryDto.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }

    public List<CategoryDto> toDto(Page<Category> categories) {
        return categories.stream()
                .map(this::toDto)
                .toList();
    }

    public Category fromDto(NewCategoryDto newCategory) {
        return Category.builder()
                .name(newCategory.getName())
                .build();
    }
}
