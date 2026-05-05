package com.finanscepte.report.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.finanscepte.common.exception.ResourceNotFoundException;
import com.finanscepte.report.model.Report;
import com.finanscepte.report.model.ReportType;
import com.finanscepte.report.repository.ReportRepository;
import com.finanscepte.report.service.impl.ReportServiceImpl;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ReportServiceTest_20260503 {

    @Mock
    private ReportRepository reportRepository;

    @InjectMocks
    private ReportServiceImpl reportService;

    @Test
    void shouldCreateReport() {
        Report request = Report.builder().userId("u1").type(ReportType.MONTHLY_SUMMARY).data("test").build();
        Report saved = Report.builder().id("1").userId("u1").type(ReportType.MONTHLY_SUMMARY).data("test").build();
        when(reportRepository.save(request)).thenReturn(saved);

        Report result = reportService.create(request);

        assertEquals("1", result.getId());
    }

    @Test
    void shouldThrowNotFoundWhenReportMissing() {
        when(reportRepository.findById("404")).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> reportService.getById("404"));
    }

    @Test
    void shouldDeleteReportById() {
        reportService.delete("9");
        verify(reportRepository).deleteById("9");
    }

    @Test
    void shouldGenerateMonthlySummaryReport() {
        Report saved = Report.builder().id("1").userId("u1").type(ReportType.MONTHLY_SUMMARY).data("Monthly summary for 5/2026").build();
        when(reportRepository.save(org.mockito.ArgumentMatchers.any(Report.class))).thenReturn(saved);

        Report result = reportService.generateReport("u1", "MONTHLY_SUMMARY", 5, 2026);

        assertEquals(ReportType.MONTHLY_SUMMARY, result.getType());
    }

    @Test
    void shouldGenerateCategoryBreakdownReport() {
        Report saved = Report.builder().id("2").userId("u1").type(ReportType.CATEGORY_BREAKDOWN).data("Category breakdown for 5/2026").build();
        when(reportRepository.save(org.mockito.ArgumentMatchers.any(Report.class))).thenReturn(saved);

        Report result = reportService.generateReport("u1", "CATEGORY_BREAKDOWN", 5, 2026);

        assertEquals(ReportType.CATEGORY_BREAKDOWN, result.getType());
    }
}
