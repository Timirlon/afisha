package com.practice.afisha.repository;

import com.practice.afisha.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

public interface UserRepository extends JpaRepository<User, Integer> {
    Page<User> findAllByIdIn(Collection<Integer> ids, Pageable pageable);
}
