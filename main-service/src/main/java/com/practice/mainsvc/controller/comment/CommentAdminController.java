package com.practice.mainsvc.controller.comment;

import com.practice.mainsvc.client.StatisticsClient;
import com.practice.mainsvc.dto.comment.CommentFullDto;
import com.practice.mainsvc.dto.report.CommentReportDto;
import com.practice.mainsvc.mapper.CommentMapper;
import com.practice.mainsvc.mapper.ReportMapper;
import com.practice.mainsvc.model.Comment;
import com.practice.mainsvc.model.CommentReport;
import com.practice.mainsvc.service.CommentService;
import com.practice.mainsvc.service.ReportService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)

@RestController
@RequestMapping("/admin/comments")
public class CommentAdminController {
    CommentService commentService;
    CommentMapper commentMapper;

    StatisticsClient statisticsClient;

    ReportService reportService;
    ReportMapper reportMapper;

    @PatchMapping("/{commentId}")
    public CommentFullDto updateVisibility(@PathVariable int commentId,
                                           @RequestParam String visibility,
                                           HttpServletRequest servletRequest) {
        Comment result = commentService.updateVisibilityById(commentId, visibility);

        statisticsClient.hit(
                String.format("/admin/comments/%d", commentId),
                servletRequest);

        return commentMapper.toFullDto(result);
    }

    @DeleteMapping("/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable int commentId,
                           HttpServletRequest servletRequest) {

        commentService.deleteByIdAdminMethod(commentId);

        statisticsClient.hit(
                String.format("/admin/comments/%d", commentId),
                servletRequest);
    }

    @GetMapping("/reports")
    public List<CommentReportDto> findReports(@RequestParam(defaultValue = "PENDING") String state,
                                              @RequestParam(defaultValue = "0") int from,
                                              @RequestParam(defaultValue = "10") int size,
                                              HttpServletRequest servletRequest) {

        Page<CommentReport> result = reportService.findAll(state, from, size);

        statisticsClient.hit(
                "/admin/comments/reports",
                servletRequest);

        return reportMapper.toDto(result);
    }

    @GetMapping("/{commentId}/reports")
    public List<CommentReportDto> findReportsByCommentId(@PathVariable int commentId,
                                                         @RequestParam(defaultValue = "0") int from,
                                                         @RequestParam(defaultValue = "10") int size,
                                                         HttpServletRequest servletRequest) {

        Page<CommentReport> result = reportService.findAllByCommentId(commentId, from, size);

        statisticsClient.hit(
                String.format("/admin/comments/%d/reports", commentId),
                servletRequest);

        return reportMapper.toDto(result);
    }
}
