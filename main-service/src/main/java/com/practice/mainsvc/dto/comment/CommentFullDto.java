package com.practice.mainsvc.dto.comment;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CommentFullDto extends CommentShortDto {
    CommentShortDto parent;

    List<CommentShortDto> replies = new ArrayList<>();

    public CommentFullDto(CommentShortDto commentShortDto) {
        this.setId(commentShortDto.getId());
        this.setAuthor(commentShortDto.getAuthor());
        this.setText(commentShortDto.getText());
        this.setEvent(commentShortDto.getEvent());
        this.setCreated(commentShortDto.getCreated());
    }
}
