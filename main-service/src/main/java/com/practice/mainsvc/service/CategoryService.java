package com.practice.mainsvc.service;

import com.practice.mainsvc.exception.NotFoundException;
import com.practice.mainsvc.model.Category;
import com.practice.mainsvc.repository.CategoryRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)

@Service
public class CategoryService {
    CategoryRepository categoryRepository;

    public Page<Category> findAll(int from, int size) {
        int pageNumber = from / size;
        Pageable pageable = PageRequest.of(pageNumber, size);

        return categoryRepository.findAll(pageable);
    }

    public Category findById(int id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(
                        String.format("Category with id=%d was not found", id)));
    }

    public Category create(Category category) {
        categoryRepository.save(category);

        return category;
    }

    public void deleteById(int id) {
        findById(id);


        categoryRepository.deleteById(id);
    }

    public Category updateById(int id, Category updateCategory) {
        Category foundCategory = findById(id);
        foundCategory.setName(updateCategory.getName());

        categoryRepository.save(foundCategory);

        return foundCategory;
    }
}
