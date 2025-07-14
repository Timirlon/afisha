package com.practice.mainsvc.service;

import com.practice.mainsvc.exception.NotFoundException;
import com.practice.mainsvc.model.Comment;
import com.practice.mainsvc.model.CommentReport;
import com.practice.mainsvc.model.ReportStatus;
import com.practice.mainsvc.model.User;
import com.practice.mainsvc.repository.CommentRepository;
import com.practice.mainsvc.repository.ReportRepository;
import com.practice.mainsvc.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor

@Service
public class ReportService {
    ReportRepository reportRepository;

    UserRepository userRepository;
    CommentRepository commentRepository;

    public CommentReport createNew(CommentReport report, int commentId, int reporterId) {
        User reporter = findUserById(reporterId);

        Comment reportedComment = findCommentById(commentId);

        report.setReporter(reporter);
        report.setComment(reportedComment);
        report.setCreated(LocalDateTime.now());
        report.setStatus(ReportStatus.PENDING);

        reportRepository.save(report);

        return report;
    }

    public Page<CommentReport> findAll(String state, int from, int size) {
        int page = from / size;
        Pageable pageable = PageRequest.of(page, size);

        return reportRepository.findAll(pageable);
    }

    public Page<CommentReport> findAllByCommentId(int commentId, int from, int size) {
        findCommentById(commentId);

        int page = from / size;
        Pageable pageable = PageRequest.of(page, size);

        return reportRepository.findAllByComment_Id(commentId, pageable);
    }

    private Comment findCommentById(int commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException(
                        String.format("Comment with id=%d was not found", commentId)));
    }

    private User findUserById(int userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(
                        String.format("User with id=%d was not found", userId)));
    }
}
