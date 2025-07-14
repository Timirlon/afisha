package com.practice.mainsvc.mapper;

import com.practice.mainsvc.dto.comment.CommentFullDto;
import com.practice.mainsvc.dto.comment.CommentShortDto;
import com.practice.mainsvc.dto.comment.CommentRequestDto;
import com.practice.mainsvc.model.Comment;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

import static com.practice.mainsvc.util.DateTimeFormatConstants.format;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)

@Component
public class CommentMapper {
    UserMapper userMapper;

    public CommentShortDto toShortDto(Comment comment) {
        CommentShortDto shortDto = new CommentShortDto();

        shortDto.setId(comment.getId());
        shortDto.setText(comment.getText());
        shortDto.setEvent(comment.getEvent().getId());
        shortDto.setAuthor(
                userMapper.toShortDto(comment.getAuthor()));

        shortDto.setCreated(
                format(comment.getCreatedAt()));

        if (comment.getUpdatedAt() != null) {
            shortDto.setUpdated(
                    format(comment.getUpdatedAt()));
        }


        return shortDto;
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

    public CommentFullDto toFullDto(Comment comment) {
        CommentFullDto fullDto = new CommentFullDto(
                toShortDto(comment));

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
