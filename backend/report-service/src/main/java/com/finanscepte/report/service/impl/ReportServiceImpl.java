package com.finanscepte.report.service.impl;

import com.finanscepte.common.exception.ResourceNotFoundException;
import com.finanscepte.report.model.Report;
import com.finanscepte.report.repository.ReportRepository;
import com.finanscepte.report.service.ReportService;
import com.finanscepte.report.strategy.ReportGenerationStrategy;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class ReportServiceImpl implements ReportService {

    private final ReportRepository reportRepository;
    private final Map<String, ReportGenerationStrategy> strategyMap;

    public ReportServiceImpl(ReportRepository reportRepository, List<ReportGenerationStrategy> strategies) {
        this.reportRepository = reportRepository;
        this.strategyMap = strategies.stream()
                .collect(Collectors.toMap(ReportGenerationStrategy::getType, s -> s));
    }

    @Override
    public Report create(Report entity) {
        return reportRepository.save(entity);
    }

    @Override
    public Report getById(String id) {
        return reportRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Report not found: " + id));
    }

    @Override
    public List<Report> getAll() {
        return reportRepository.findAll();
    }

    @Override
    public void delete(String id) {
        reportRepository.deleteById(id);
    }

    @Override
    public List<Report> findByUserId(String userId) {
        return reportRepository.findByUserId(userId);
    }

    @Override
    public Report generateReport(String userId, String typeStr, int month, int year) {
        ReportGenerationStrategy strategy = strategyMap.get(typeStr);
        if (strategy == null) {
            throw new IllegalArgumentException("Unknown report type: " + typeStr);
        }
        Report report = strategy.generate(userId, month, year);
        return reportRepository.save(report);
    }
}
