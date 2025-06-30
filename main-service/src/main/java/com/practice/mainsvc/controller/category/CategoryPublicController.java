package com.practice.mainsvc.controller.category;

import com.practice.mainsvc.dto.category.CategoryDto;
import com.practice.mainsvc.mapper.CategoryMapper;
import com.practice.mainsvc.service.CategoryService;
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
    public List<CategoryDto> findAll(@RequestParam(defaultValue = "0") int from,
                                     @RequestParam(defaultValue = "10") int size) {
        return categoryMapper.toDto(
                categoryService.findAll(from, size));
    }

    @GetMapping("/{categoryId}")
    public CategoryDto findById(@PathVariable int categoryId) {
        return categoryMapper.toDto(
                categoryService.findById(categoryId));
    }
}
