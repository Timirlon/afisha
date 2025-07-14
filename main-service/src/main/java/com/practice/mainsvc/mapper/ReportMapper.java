package com.practice.mainsvc.mapper;

import com.practice.mainsvc.dto.report.CommentReportDto;
import com.practice.mainsvc.dto.report.NewCommentReportDto;
import com.practice.mainsvc.model.Comment;
import com.practice.mainsvc.model.CommentReport;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.practice.mainsvc.util.DateTimeFormatConstants.format;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor

@Component
public class ReportMapper {
    UserMapper userMapper;

    public CommentReport fromDto(NewCommentReportDto dto) {
        CommentReport model = new CommentReport();

        model.setReason(dto.getReason());

        return model;
    }

    public CommentReportDto toDto(CommentReport report) {
        CommentReportDto dto = new CommentReportDto();

        dto.setId(report.getId());
        dto.setReason(report.getReason());
        dto.setComment(report.getComment().getId());
        dto.setStatus(report.getStatus().name());
        dto.setCreated(
                format(report.getCreated()));
        dto.setReporter(
                userMapper.toShortDto(report.getReporter()));


        return dto;
    }

    public List<CommentReportDto> toDto(Page<CommentReport> reports) {
        return reports.stream()
                .map(this::toDto)
                .toList();
    }
}
