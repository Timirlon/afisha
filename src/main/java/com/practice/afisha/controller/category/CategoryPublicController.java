package com.practice.afisha.controller.category;

import com.practice.afisha.dto.category.CategoryDto;
import com.practice.afisha.mapper.CategoryMapper;
import com.practice.afisha.service.CategoryService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)

@RestController
@RequestMapping("/categories")
public class CategoryPublicController {
    CategoryService categoryService;
    CategoryMapper categoryMapper;

    @GetMapping
    public List<CategoryDto> findAll(@RequestParam int from, @RequestParam int size) {
        return categoryMapper.toDto(
                categoryService.findAll(from, size));
    }

    @GetMapping("/{categoryId}")
    public CategoryDto findById(@PathVariable int categoryId) {
        return categoryMapper.toDto(
                categoryService.findById(categoryId));
    }
}
