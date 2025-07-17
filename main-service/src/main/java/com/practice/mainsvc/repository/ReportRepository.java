package com.practice.mainsvc.repository;

import com.practice.mainsvc.model.CommentReport;
import com.practice.mainsvc.model.ReportStatus;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

public interface ReportRepository extends JpaRepository<CommentReport, Integer> {
    Page<CommentReport> findAllByComment_Id(int commentId, Pageable pageable);

    Page<CommentReport> findAllByStatus(ReportStatus status, Pageable pageable);

    Collection<CommentReport> findAllByComment_IdIn(Collection<Integer> commentId);

    Collection<CommentReport> findAllByIdIn(Collection<Integer> ids);

    @Transactional
    void deleteAllByComment_IdOrComment_Parent_Id(int commentId, int parentId);

    @Transactional
    void deleteAllByComment_IdInOrComment_Parent_IdIn(Collection<Integer> commentIds, Collection<Integer> parentIds);
}
