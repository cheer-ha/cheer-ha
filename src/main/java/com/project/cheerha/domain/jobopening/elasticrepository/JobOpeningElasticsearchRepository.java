package com.project.cheerha.domain.jobopening.elasticrepository;

import com.project.cheerha.domain.jobopening.entity.JobOpeningDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface JobOpeningElasticsearchRepository extends ElasticsearchRepository<JobOpeningDocument, String> {

}

