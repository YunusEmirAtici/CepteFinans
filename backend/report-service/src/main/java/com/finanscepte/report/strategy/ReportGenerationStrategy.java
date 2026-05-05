package com.finanscepte.report.strategy;

import com.finanscepte.report.model.Report;

public interface ReportGenerationStrategy {
    String getType();
    Report generate(String userId, int month, int year);
}
