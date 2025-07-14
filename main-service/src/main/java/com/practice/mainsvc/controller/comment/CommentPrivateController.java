package com.practice.mainsvc.controller.comment;

import com.practice.mainsvc.client.StatisticsClient;
import com.practice.mainsvc.dto.comment.CommentFullDto;
import com.practice.mainsvc.dto.comment.CommentRequestDto;
import com.practice.mainsvc.dto.report.CommentReportDto;
import com.practice.mainsvc.dto.report.NewCommentReportDto;
import com.practice.mainsvc.mapper.CommentMapper;
import com.practice.mainsvc.mapper.ReportMapper;
import com.practice.mainsvc.model.Comment;
import com.practice.mainsvc.model.CommentReport;
import com.practice.mainsvc.service.CommentService;
import com.practice.mainsvc.service.ReportService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)

@RestController
@RequestMapping("/users/{userId}/comments")
public class CommentPrivateController {
    CommentService commentService;
    CommentMapper commentMapper;

    StatisticsClient statisticsClient;

    ReportService reportService;
    ReportMapper reportMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CommentFullDto createNew(@RequestBody @Valid CommentRequestDto commentDto,
                                    @PathVariable int userId,
                                    @RequestParam(name = "event") int eventId,
                                    HttpServletRequest servletRequest) {
        Comment commentRequest = commentMapper.fromDto(commentDto);
        Integer parentId = commentDto.getParent();

        Comment result = commentService.createNew(commentRequest, parentId, userId, eventId);

        statisticsClient.hit(
                String.format("/users/%d/comments?event=%d", userId, eventId),
                servletRequest);

        return commentMapper.toFullDto(result);
    }

    @PatchMapping("/{commentId}")
    public CommentFullDto updateById(@RequestBody @Valid CommentRequestDto commentDto,
                                     @PathVariable int commentId,
                                     @PathVariable int userId,
                                     HttpServletRequest servletRequest) {

        Comment commentRequest = commentMapper.fromDto(commentDto);

        Comment result = commentService.updateById(commentRequest, commentId, userId);

        statisticsClient.hit(
                String.format("/users/%d/comments/%d", userId, commentId),
                servletRequest);


        return commentMapper.toFullDto(result);
    }

    @DeleteMapping("/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable int commentId,
                           @PathVariable int userId,
                           HttpServletRequest servletRequest) {

        commentService.deleteByIdPrivateRequest(commentId, userId);

        statisticsClient.hit(
                String.format("/users/%d/comments/%d", userId, commentId),
                servletRequest);
    }

    @PostMapping("/{commentId}/report")
    @ResponseStatus(HttpStatus.CREATED)
    public CommentReportDto createNewReport(@RequestBody @Valid NewCommentReportDto reportDto,
                                            @PathVariable int commentId,
                                            @PathVariable int userId,
                                            HttpServletRequest servletRequest) {
        CommentReport report = reportMapper.fromDto(reportDto);

        CommentReport result = reportService.createNew(report, commentId, userId);

        statisticsClient.hit(
                String.format("/users/%d/comments/%d/report", userId, commentId),
                servletRequest);


        return reportMapper.toDto(result);
    }
}
