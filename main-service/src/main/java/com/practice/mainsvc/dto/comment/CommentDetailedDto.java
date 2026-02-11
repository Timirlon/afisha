package com.practice.mainsvc.dto.comment;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CommentDetailedDto extends CommentShortDto {
    Boolean hidden;

    public CommentDetailedDto(CommentShortDto shortDto) {
        super(
                shortDto.getId(),
                shortDto.getText(),
                shortDto.getEvent(),
                shortDto.getAuthor(),
                shortDto.getCreated(),
                shortDto.getUpdated());
    }
}
