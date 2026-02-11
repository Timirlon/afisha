package com.practice.mainsvc.dto.report;

import com.practice.mainsvc.dto.user.UserShortDto;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class CommentReportDto {
    int id;

    String reason;

    String created;

    Integer comment;

    UserShortDto reporter;

    String status;
}
