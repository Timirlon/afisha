package com.practice.mainsvc.controller.category;

import com.practice.mainsvc.dto.category.CategoryDto;
import com.practice.mainsvc.dto.category.NewCategoryDto;
import com.practice.mainsvc.mapper.CategoryMapper;
import com.practice.mainsvc.model.Category;
import com.practice.mainsvc.service.CategoryService;
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
