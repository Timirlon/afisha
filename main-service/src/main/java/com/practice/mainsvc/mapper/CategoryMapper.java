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
        CategoryDto categoryDto = new CategoryDto();

        categoryDto.setId(category.getId());
        categoryDto.setName(category.getName());


        return categoryDto;
    }

    public List<CategoryDto> toDto(Page<Category> categories) {
        return categories.stream()
                .map(this::toDto)
                .toList();
    }

    public Category fromDto(NewCategoryDto newCategory) {
        Category category = new Category();

        category.setName(newCategory.getName());

        return category;
    }
}
