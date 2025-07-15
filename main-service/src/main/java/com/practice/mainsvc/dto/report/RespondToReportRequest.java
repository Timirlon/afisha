package com.practice.mainsvc.dto.report;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.Collection;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RespondToReportRequest {
    Collection<Integer> reports;

    String action;
}
