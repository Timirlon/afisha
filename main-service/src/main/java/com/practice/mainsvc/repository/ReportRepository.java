package com.practice.mainsvc.repository;

import com.practice.mainsvc.model.CommentReport;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRepository extends JpaRepository<CommentReport, Integer> {
    Page<CommentReport> findAllByComment_Id(int commentId, Pageable pageable);
}
