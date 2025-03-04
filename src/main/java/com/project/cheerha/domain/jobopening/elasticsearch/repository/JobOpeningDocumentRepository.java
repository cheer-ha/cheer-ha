package com.project.cheerha.domain.jobopening.elasticsearch.repository;

import com.project.cheerha.domain.jobopening.elasticsearch.entity.JobOpeningDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface JobOpeningDocumentRepository extends ElasticsearchRepository<JobOpeningDocument, String> {
}
