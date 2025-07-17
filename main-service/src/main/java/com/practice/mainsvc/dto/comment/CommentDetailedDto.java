package com.practice.mainsvc.dto.comment;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CommentDetailedDto extends CommentShortDto {
    Boolean hidden;

    public CommentDetailedDto(CommentShortDto detailedDto) {
        this.setId(detailedDto.getId());
        this.setAuthor(detailedDto.getAuthor());
        this.setText(detailedDto.getText());
        this.setEvent(detailedDto.getEvent());
        this.setCreated(detailedDto.getCreated());
        this.setUpdated(detailedDto.getUpdated());
    }
}
