package com.finanscepte.report.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import com.finanscepte.report.dto.ReportResponse;
import com.finanscepte.report.model.Report;
import com.finanscepte.report.model.ReportType;
import com.finanscepte.report.service.ReportService;
import com.finanscepte.report.util.ReportMapper;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ReportControllerTest_20260503 {

    @Mock
    private ReportService reportService;

    @Mock
    private ReportMapper reportMapper;

    @InjectMocks
    private ReportController reportController;

    @Test
    void shouldReturnReportList() {
        Report report = Report.builder().id("1").userId("u1").type(ReportType.MONTHLY_SUMMARY).data("summary").build();
        ReportResponse response = new ReportResponse("1", "u1", ReportType.MONTHLY_SUMMARY, null, null, "summary", null);
        when(reportService.getAll()).thenReturn(List.of(report));
        when(reportMapper.toResponse(report)).thenReturn(response);

        List<ReportResponse> reports = reportController.getAll();

        assertEquals(1, reports.size());
    }
}
