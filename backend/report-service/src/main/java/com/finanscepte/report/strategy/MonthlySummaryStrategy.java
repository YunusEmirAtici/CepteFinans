package com.finanscepte.report.strategy;

import com.finanscepte.report.model.Report;
import com.finanscepte.report.model.ReportType;
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.springframework.stereotype.Component;

@Component
public class MonthlySummaryStrategy implements ReportGenerationStrategy {

    @Override
    public String getType() {
        return "MONTHLY_SUMMARY";
    }

    @Override
    public Report generate(String userId, int month, int year) {
        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());

        String data = String.format(
            "Aylik Ozet Rapor - %d/%d\nDonem: %s - %s\nKullanici: %s\nToplam Gelir/Gider ozeti...",
            month, year, startDate, endDate, userId
        );

        return Report.builder()
                .userId(userId)
                .type(ReportType.MONTHLY_SUMMARY)
                .startDate(startDate)
                .endDate(endDate)
                .data(data)
                .createdAt(LocalDateTime.now())
                .build();
    }
}
