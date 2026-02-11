package com.practice.mainsvc.mapper;

import com.practice.mainsvc.dto.report.CommentReportDto;
import com.practice.mainsvc.dto.report.NewCommentReportDto;
import com.practice.mainsvc.model.CommentReport;
import com.practice.mainsvc.util.DateTimeFormatConstants;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor

@Component
public class ReportMapper {
    UserMapper userMapper;

    public CommentReport fromDto(NewCommentReportDto dto) {
        return CommentReport.builder()
                .reason(dto.getReason())
                .build();
    }

    public CommentReportDto toDto(CommentReport report) {
        return CommentReportDto
                .builder()
                .id(report.getId())
                .reason(report.getReason())
                .comment(report.getComment().getId())
                .status(report.getStatus().name())
                .created(DateTimeFormatConstants.format(
                        report.getCreated()))
                .reporter(userMapper.toShortDto(
                        report.getReporter()))
                .build();
    }

    public List<CommentReportDto> toDto(Page<CommentReport> reports) {
        return reports.stream()
                .map(this::toDto)
                .toList();
    }

    public List<CommentReportDto> toDto(Collection<CommentReport> reports) {
        return reports.stream()
                .map(this::toDto)
                .toList();
    }
}
