package com.finanscepte.report.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.finanscepte.report.model.Report;
import com.finanscepte.report.model.ReportType;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@DataMongoTest
@Testcontainers
class ReportRepositoryIT_20260503 {

    @Container
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:7.0");

    @DynamicPropertySource
    static void mongoProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }

    @Autowired
    private ReportRepository reportRepository;

    @Test
    void shouldPersistAndReadReport() {
        Report saved = reportRepository.save(Report.builder()
                .userId("u1")
                .type(ReportType.MONTHLY_SUMMARY)
                .startDate(LocalDate.of(2026, 5, 1))
                .endDate(LocalDate.of(2026, 5, 31))
                .data("Summary data")
                .createdAt(LocalDateTime.now())
                .build());

        Report found = reportRepository.findById(saved.getId()).orElseThrow();
        assertEquals(ReportType.MONTHLY_SUMMARY, found.getType());
        assertEquals("Summary data", found.getData());
    }

    @Test
    void shouldFindByUserId() {
        reportRepository.save(Report.builder().userId("u1").type(ReportType.MONTHLY_SUMMARY).startDate(LocalDate.now()).endDate(LocalDate.now()).data("R1").createdAt(LocalDateTime.now()).build());
        reportRepository.save(Report.builder().userId("u1").type(ReportType.CATEGORY_BREAKDOWN).startDate(LocalDate.now()).endDate(LocalDate.now()).data("R2").createdAt(LocalDateTime.now()).build());
        reportRepository.save(Report.builder().userId("u2").type(ReportType.MONTHLY_SUMMARY).startDate(LocalDate.now()).endDate(LocalDate.now()).data("R3").createdAt(LocalDateTime.now()).build());

        List<Report> found = reportRepository.findByUserId("u1");
        assertEquals(2, found.size());
    }

    @Test
    void shouldDeleteReport() {
        Report saved = reportRepository.save(Report.builder()
                .userId("u1")
                .type(ReportType.MONTHLY_SUMMARY)
                .startDate(LocalDate.of(2026, 1, 1))
                .endDate(LocalDate.of(2026, 1, 31))
                .data("DeleteMe")
                .createdAt(LocalDateTime.now())
                .build());

        reportRepository.deleteById(saved.getId());
        assertTrue(reportRepository.findById(saved.getId()).isEmpty());
    }
}
