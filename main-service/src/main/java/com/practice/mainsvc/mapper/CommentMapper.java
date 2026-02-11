package com.practice.mainsvc.mapper;

import com.practice.mainsvc.dto.comment.CommentDetailedDto;
import com.practice.mainsvc.dto.comment.CommentFullDto;
import com.practice.mainsvc.dto.comment.CommentShortDto;
import com.practice.mainsvc.dto.comment.CommentRequestDto;
import com.practice.mainsvc.model.Comment;
import com.practice.mainsvc.util.DateTimeFormatConstants;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)

@Component
public class CommentMapper {
    UserMapper userMapper;

    public CommentShortDto toShortDto(Comment comment) {
        return CommentShortDto.builder()
                .id(comment.getId())
                .text(comment.getText())
                .event(comment.getEvent().getId())
                .author(userMapper.toShortDto(
                        comment.getAuthor()))
                .created(DateTimeFormatConstants.format(
                        comment.getCreatedAt()))
                .updated(DateTimeFormatConstants.format(
                        comment.getUpdatedAt()))
                .build();
    }

    public List<CommentShortDto> toShortDto(Page<Comment> comments) {
        return comments.stream()
                .map(this::toShortDto)
                .toList();
    }

    public List<CommentShortDto> toShortDto(Collection<Comment> comments) {
        return comments.stream()
                .map(this::toShortDto)
                .toList();
    }

    public CommentDetailedDto toDetailedDto(Comment comment) {
        CommentDetailedDto detailedDto = new CommentDetailedDto(
                toShortDto(comment));

        detailedDto.setHidden(comment.getHidden());

        return detailedDto;
    }

    public CommentFullDto toFullDto(Comment comment) {
        CommentFullDto fullDto = new CommentFullDto(
                toDetailedDto(comment));

        if (comment.getParent() != null) {
            fullDto.setParent(
                    toShortDto(comment.getParent()));
        }

        if (comment.getSubcomments() != null
                && !comment.getSubcomments().isEmpty()) {
            fullDto.setReplies(
                    toShortDto(comment.getSubcomments()));
        }

        return fullDto;
    }

    public Comment fromDto(CommentRequestDto dto) {
        Comment comment = new Comment();

        comment.setText(dto.getText());

        return comment;
    }
}
