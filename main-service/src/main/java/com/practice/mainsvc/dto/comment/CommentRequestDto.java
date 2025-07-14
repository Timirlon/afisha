package com.practice.mainsvc.dto.comment;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CommentRequestDto {
    @NotEmpty
    @Size(min = 1, max = 3000)
    String text;

    Integer parent;
}
