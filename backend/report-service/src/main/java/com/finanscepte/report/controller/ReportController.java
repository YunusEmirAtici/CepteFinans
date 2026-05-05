package com.finanscepte.report.controller;

import com.finanscepte.report.dto.ReportRequest;
import com.finanscepte.report.dto.ReportResponse;
import com.finanscepte.report.service.ReportService;
import com.finanscepte.report.util.ReportMapper;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;
    private final ReportMapper reportMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ReportResponse create(@Valid @RequestBody ReportRequest request) {
        return reportMapper.toResponse(reportService.create(reportMapper.toEntity(request)));
    }

    @GetMapping("/{id}")
    public ReportResponse getById(@PathVariable String id) {
        return reportMapper.toResponse(reportService.getById(id));
    }

    @GetMapping
    public List<ReportResponse> getAll() {
        return reportService.getAll().stream().map(reportMapper::toResponse).toList();
    }

    @GetMapping("/user/{userId}")
    public List<ReportResponse> getByUserId(@PathVariable String userId) {
        return reportService.findByUserId(userId).stream().map(reportMapper::toResponse).toList();
    }

    @PostMapping("/generate")
    public ReportResponse generateReport(
            @RequestParam String userId,
            @RequestParam String type,
            @RequestParam int month,
            @RequestParam int year) {
        return reportMapper.toResponse(reportService.generateReport(userId, type, month, year));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String id) {
        reportService.delete(id);
    }

    @GetMapping("/health")
    public Map<String, String> health() {
        return Map.of("service", "report-service", "status", "ok");
    }
}
