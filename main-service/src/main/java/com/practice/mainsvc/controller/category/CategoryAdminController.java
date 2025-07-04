package com.practice.mainsvc.controller.category;

import com.practice.mainsvc.client.StatisticsClient;
import com.practice.mainsvc.dto.category.CategoryDto;
import com.practice.mainsvc.dto.category.NewCategoryDto;
import com.practice.mainsvc.mapper.CategoryMapper;
import com.practice.mainsvc.model.Category;
import com.practice.mainsvc.service.CategoryService;
import jakarta.servlet.http.HttpServletRequest;
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

    StatisticsClient statisticsClient;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto create(@RequestBody @Valid NewCategoryDto categoryRequest,
                              HttpServletRequest servletRequest) {
        Category category = categoryMapper.fromDto(categoryRequest);

        CategoryDto result = categoryMapper.toDto(
                categoryService.create(category));

        statisticsClient.hit("/admin/categories", servletRequest);


        return result;
    }

    @DeleteMapping("/{categoryId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable int categoryId,
                           HttpServletRequest servletRequest) {
        categoryService.deleteById(categoryId);

        statisticsClient.hit(
                String.format("/admin/categories/%d", categoryId),
                servletRequest);
    }

    @PatchMapping("/{categoryId}")
    public CategoryDto updateById(@PathVariable int categoryId,
                                  @RequestBody @Valid NewCategoryDto categoryRequest,
                                  HttpServletRequest servletRequest) {

        Category category = categoryMapper.fromDto(categoryRequest);

        CategoryDto result = categoryMapper.toDto(
                categoryService.updateById(categoryId, category));

        statisticsClient.hit(
                String.format("/admin/categories/%d", categoryId),
                servletRequest);


        return result;
    }
}
