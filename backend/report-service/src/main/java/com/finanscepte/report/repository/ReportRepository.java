package com.finanscepte.report.repository;

import com.finanscepte.report.model.Report;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ReportRepository extends MongoRepository<Report, String> {
    List<Report> findByUserId(String userId);
}
