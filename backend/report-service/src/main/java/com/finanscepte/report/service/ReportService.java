package com.finanscepte.report.service;

import com.finanscepte.common.service.GenericService;
import com.finanscepte.report.model.Report;
import java.util.List;

public interface ReportService extends GenericService<Report, String> {
    List<Report> findByUserId(String userId);
    Report generateReport(String userId, String type, int month, int year);
}
