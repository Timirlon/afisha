package com.practice.mainsvc.repository;

import com.practice.mainsvc.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

public interface UserRepository extends JpaRepository<User, Integer> {
    Page<User> findAllOrderBy(Pageable pageable);

    Page<User> findAllByIdIn(Collection<Integer> ids, Pageable pageable);
}
