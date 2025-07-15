package com.practice.mainsvc.repository;

import com.practice.mainsvc.model.CommentReport;
import com.practice.mainsvc.model.ReportStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

public interface ReportRepository extends JpaRepository<CommentReport, Integer> {
    Page<CommentReport> findAllByComment_Id(int commentId, Pageable pageable);

    Page<CommentReport> findAllByStatus(ReportStatus status, Pageable pageable);

    Collection<CommentReport> findAllByComment_IdIn(Collection<Integer> commentId);

    Collection<CommentReport> findAllByIdIn(Collection<Integer> ids);

    void deleteAllByComment_Id(int commentId);

    void deleteAllByComment_IdIn(Collection<Integer> commentIds);
}
