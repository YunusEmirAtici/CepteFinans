package com.finanscepte.report.dto;

import com.finanscepte.report.model.ReportType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record ReportRequest(
        @NotBlank(message = "userId bos olamaz") String userId,
        @NotNull(message = "type bos olamaz") ReportType type,
        @NotNull(message = "startDate bos olamaz") LocalDate startDate,
        @NotNull(message = "endDate bos olamaz") LocalDate endDate
) {
}
