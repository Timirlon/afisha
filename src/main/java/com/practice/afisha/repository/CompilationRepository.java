package com.practice.afisha.repository;

import com.practice.afisha.model.Compilation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompilationRepository extends JpaRepository<Compilation, Integer> {
    Page<Compilation> findAllByPinned(boolean pinned, Pageable pageable);
}
