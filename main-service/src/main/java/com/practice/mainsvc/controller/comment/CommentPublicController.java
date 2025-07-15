package com.practice.mainsvc.controller.comment;

import com.practice.mainsvc.client.StatisticsClient;
import com.practice.mainsvc.dto.comment.CommentFullDto;
import com.practice.mainsvc.dto.comment.CommentShortDto;
import com.practice.mainsvc.mapper.CommentMapper;
import com.practice.mainsvc.model.Comment;
import com.practice.mainsvc.service.CommentService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)

@RestController
@RequestMapping("/comments")
public class CommentPublicController {
    CommentService commentService;
    CommentMapper commentMapper;

    StatisticsClient statisticsClient;

    @GetMapping
    public List<CommentShortDto> findAllByEvent(@RequestParam(name = "event") int eventId,
                                                @RequestParam(defaultValue = "0") int from,
                                                @RequestParam(defaultValue = "10") int size,
                                                @RequestParam(defaultValue = "false") boolean showHidden,
                                                HttpServletRequest servletRequest) {

        Page<Comment> result = commentService.findAllByEvent(eventId, from, size, showHidden);

        statisticsClient.hit(String.format("/comments?event=%d", eventId), servletRequest);

        return commentMapper.toShortDto(result);
    }

    @GetMapping("/{commentId}")
    public CommentFullDto findById(@PathVariable int commentId,
                                   @RequestParam(defaultValue = "false") boolean showHidden,
                                   HttpServletRequest servletRequest) {

        Comment result = commentService.findByIdWithRepliesAndParent(commentId, showHidden);

        statisticsClient.hit(
                String.format("/comments/%d", commentId),
                servletRequest);

        return commentMapper.toFullDto(result);
    }

    @GetMapping("/{commentId}/replies")
    public List<CommentShortDto> findReplies(@PathVariable int commentId,
                                             @RequestParam(defaultValue = "0") int from,
                                             @RequestParam(defaultValue = "10") int size,
                                             @RequestParam(defaultValue = "false") boolean showHidden,
                                             HttpServletRequest servletRequest) {

        Page<Comment> result = commentService.findRepliesByParentId(commentId, from, size, showHidden);

        statisticsClient.hit(
                String.format("/comments/%d/replies", commentId),
                servletRequest);

        return commentMapper.toShortDto(result);
    }
}
