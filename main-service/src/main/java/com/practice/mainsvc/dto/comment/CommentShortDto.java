package com.practice.mainsvc.dto.comment;

import com.practice.mainsvc.dto.user.UserShortDto;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class CommentShortDto {
    int id;

    String text;

    Integer event;

    UserShortDto author;

    String created;

    String updated;
}
