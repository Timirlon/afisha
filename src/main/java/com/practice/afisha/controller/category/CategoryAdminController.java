package com.practice.afisha.controller.category;

import com.practice.afisha.dto.category.CategoryDto;
import com.practice.afisha.dto.category.NewCategoryDto;
import com.practice.afisha.mapper.CategoryMapper;
import com.practice.afisha.model.Category;
import com.practice.afisha.service.CategoryService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)

@RestController
@RequestMapping("/admin/categories")
public class CategoryAdminController {
    CategoryMapper categoryMapper;
    CategoryService categoryService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto create(@RequestBody @Valid NewCategoryDto categoryRequest) {
        Category category = categoryMapper.fromDto(categoryRequest);

        return categoryMapper.toDto(
                categoryService.create(category));
    }

    @DeleteMapping("/{categoryId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable int categoryId) {
        categoryService.deleteById(categoryId);
    }

    @PatchMapping("/{categoryId}")
    public CategoryDto updateById(@PathVariable int categoryId,
                              @RequestBody @Valid NewCategoryDto categoryRequest) {

        Category category = categoryMapper.fromDto(categoryRequest);

        return categoryMapper.toDto(
                categoryService.updateById(categoryId, category));
    }
}
