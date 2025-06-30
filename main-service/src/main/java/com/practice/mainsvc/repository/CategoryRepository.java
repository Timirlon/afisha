package com.practice.mainsvc.repository;

import com.practice.mainsvc.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
}
