package com.practice.mainsvc.dto.comment;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CommentFullDto extends CommentDetailedDto {
    CommentShortDto parent;

    List<CommentShortDto> replies = new ArrayList<>();

    public CommentFullDto(CommentDetailedDto detailedDto) {
        super(detailedDto);
        this.setHidden(detailedDto.getHidden());
    }
}
