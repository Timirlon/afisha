package com.practice.mainsvc.dto.comment;

import com.practice.mainsvc.dto.user.UserShortDto;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CommentShortDto {
    int id;

    String text;

    Integer event;

    UserShortDto author;

    String created;

    String updated;
}
