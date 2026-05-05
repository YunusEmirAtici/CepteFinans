package com.finanscepte.report.util;

import com.finanscepte.report.dto.ReportRequest;
import com.finanscepte.report.dto.ReportResponse;
import com.finanscepte.report.model.Report;
import java.time.LocalDateTime;
import org.springframework.stereotype.Component;

@Component
public class ReportMapper {

    public Report toEntity(ReportRequest request) {
        return Report.builder()
                .userId(request.userId())
                .type(request.type())
                .startDate(request.startDate())
                .endDate(request.endDate())
                .data("")
                .createdAt(LocalDateTime.now())
                .build();
    }

    public ReportResponse toResponse(Report report) {
        return new ReportResponse(
                report.getId(),
                report.getUserId(),
                report.getType(),
                report.getStartDate(),
                report.getEndDate(),
                report.getData(),
                report.getCreatedAt()
        );
    }
}
