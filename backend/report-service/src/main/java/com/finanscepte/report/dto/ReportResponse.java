package com.finanscepte.report.dto;

import com.finanscepte.report.model.ReportType;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record ReportResponse(
        String id,
        String userId,
        ReportType type,
        LocalDate startDate,
        LocalDate endDate,
        String data,
        LocalDateTime createdAt
) {
}
