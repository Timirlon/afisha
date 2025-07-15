package com.practice.mainsvc.service;

import com.practice.mainsvc.controller.comment.ReportAction;
import com.practice.mainsvc.controller.comment.ReportSort;
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
import java.util.Collection;

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

    public Page<CommentReport> findAll(ReportSort sort, int from, int size) {
        int page = from / size;
        Pageable pageable = PageRequest.of(page, size);

        if (sort == ReportSort.ALL) {
            return reportRepository.findAll(pageable);
        }

        ReportStatus status = null;
        if (sort == ReportSort.PENDING) {
            status = ReportStatus.PENDING;
        } else if (sort == ReportSort.APPROVED) {
            status = ReportStatus.APPROVED;
        } else if (sort == ReportSort.REJECTED) {
            status = ReportStatus.REJECTED;
        }

        return reportRepository.findAllByStatus(status, pageable);
    }

    public Page<CommentReport> findAllByCommentId(int commentId, int from, int size) {
        findCommentById(commentId);

        int page = from / size;
        Pageable pageable = PageRequest.of(page, size);

        return reportRepository.findAllByComment_Id(commentId, pageable);
    }

    public Collection<CommentReport> respond(Collection<Integer> reportIds, ReportAction action) {
        Collection<CommentReport> reports = reportRepository.findAllByIdIn(reportIds);

        if (action == ReportAction.REJECT) {
            reports.forEach(report -> report.setStatus(ReportStatus.REJECTED));

            reportRepository.saveAll(reports);
            return reports;
        }

        if (action == ReportAction.HIDE) {
            Collection<Integer> commentIds = reports.stream()
                    .map(report -> report.getComment().getId())
                    .toList();

            Collection<Comment> comments = commentRepository.findAllByIdIn(commentIds).stream()
                    .peek(comment -> comment.setHidden(true))
                    .toList();

            Collection<CommentReport> foundReports = reportRepository.findAllByComment_IdIn(commentIds)
                    .stream()
                    .peek(report -> report.setStatus(ReportStatus.APPROVED))
                    .toList();

            commentRepository.saveAll(comments);

            reportRepository.saveAll(foundReports);

            return foundReports;
        }

        if (action == ReportAction.DELETE) {
            Collection<Integer> commentIds = reports.stream()
                    .map(report -> report.getComment().getId())
                    .toList();

            Collection<CommentReport> foundReports = reportRepository.findAllByComment_IdIn(commentIds);
            reportRepository.deleteAllByComment_IdIn(commentIds);

            commentRepository.deleteAllByIdIn(commentIds);

            return foundReports;
        }

        return reports;
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
