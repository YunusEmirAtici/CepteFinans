package com.finanscepte.report.strategy;

import com.finanscepte.report.model.Report;
import com.finanscepte.report.model.ReportType;
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.springframework.stereotype.Component;

@Component
public class CategoryBreakdownStrategy implements ReportGenerationStrategy {

    @Override
    public String getType() {
        return "CATEGORY_BREAKDOWN";
    }

    @Override
    public Report generate(String userId, int month, int year) {
        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());

        String data = String.format(
            "Kategori Kirilim Raporu - %d/%d\nDonem: %s - %s\nKullanici: %s\nKategorilere gore harcama dagilimi...",
            month, year, startDate, endDate, userId
        );

        return Report.builder()
                .userId(userId)
                .type(ReportType.CATEGORY_BREAKDOWN)
                .startDate(startDate)
                .endDate(endDate)
                .data(data)
                .createdAt(LocalDateTime.now())
                .build();
    }
}
