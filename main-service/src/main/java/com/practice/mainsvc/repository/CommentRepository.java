package com.practice.mainsvc.repository;

import com.practice.mainsvc.model.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
    // поиск комментов
    Page<Comment> findAllByEvent_IdAndHidden(int eventId, Boolean isHidden, Pageable pageable);

    Page<Comment> findAllByEvent_Id(int eventId, Pageable pageable);

    // поиск ответов (подкомменты)
    Page<Comment> findAllByParent_IdAndHidden(int parentId, Boolean isHidden, Pageable pageable);

    Page<Comment> findAllByParent_Id(int parentId, Pageable pageable);

    // поиск комментов с ответами
    @Query("""
    SELECT c FROM Comment c
    JOIN FETCH c.subcomments
    JOIN FETCH c.parent
    WHERE c.id = :id
    """)
    Optional<Comment> findByIdWithAllReplies(int id);

    @Query("""
    SELECT c FROM Comment c
    JOIN FETCH c.subcomments s
    JOIN FETCH c.parent
    WHERE c.id = :id
    AND s.hidden IS FALSE
    """)
    Optional<Comment> findByIdWithoutHiddenReplies(int id);

    @Query("""
    SELECT c FROM Comment c
    JOIN FETCH c.parent
    WHERE c.id = :id
    """)
    Optional<Comment> findByIdWithParent(int id);


    @Modifying
    @Query("""
    DELETE FROM Comment c
    WHERE c.parent.id = :id
    OR c.id = :id
    """)
    void deleteByIdAndAllRelated(int id);
}
