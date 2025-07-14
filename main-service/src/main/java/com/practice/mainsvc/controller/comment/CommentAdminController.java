package com.practice.mainsvc.controller.comment;

import com.practice.mainsvc.client.StatisticsClient;
import com.practice.mainsvc.dto.comment.CommentFullDto;
import com.practice.mainsvc.mapper.CommentMapper;
import com.practice.mainsvc.model.Comment;
import com.practice.mainsvc.service.CommentService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)

@RestController
@RequestMapping("/admin/comments")
public class CommentAdminController {
    CommentService commentService;
    CommentMapper commentMapper;

    StatisticsClient statisticsClient;

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
}
