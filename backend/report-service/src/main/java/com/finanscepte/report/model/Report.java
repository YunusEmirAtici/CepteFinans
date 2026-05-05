package com.finanscepte.report.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@Document(collection = "reports")
public class Report {
    @Id
    private String id;
    private String userId;
    private ReportType type;
    private LocalDate startDate;
    private LocalDate endDate;
    private String data;
    private LocalDateTime createdAt;
}
