package com.project.cheerha.domain.jobopening.data.mysql;

import com.project.cheerha.domain.jobopening.entity.JobOpening;
import com.project.cheerha.domain.jobopening.entity.JobOpeningDocument;
import com.project.cheerha.domain.keyword.entity.JobOpeningKeyword;
import com.project.cheerha.domain.keyword.entity.Keyword;
import com.project.cheerha.domain.jobopening.elasticrepository.JobOpeningDocumentRepository;
import com.project.cheerha.domain.jobopening.repository.JobOpeningRepository;
import com.project.cheerha.domain.keyword.repository.JobOpeningKeywordRepository;
import com.project.cheerha.domain.keyword.repository.KeywordRepository;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class MysqlToElasticsearchService {

    @Autowired
    private JobOpeningRepository jobOpeningRepository;

    @Autowired
    private JobOpeningKeywordRepository jobOpeningKeywordRepository;

    @Autowired
    private KeywordRepository keywordRepository;

    @Autowired
    private JobOpeningDocumentRepository jobOpeningDocumentRepository;

    @PostConstruct
    public void init() {
        syncDataToElasticsearch();  // MySQL에서 Elasticsearch로 동기화하는 메서드를 초기화 시점에 호출
    }

    /**
     * MySQL에서 데이터를 조회하여 Elasticsearch에 저장하는 메서드
     */
    @Transactional
    public void syncDataToElasticsearch() {
        try {
            log.info("MySQL에서 데이터를 조회하는 중...");
            // @Query 사용하여 JobOpening과 그에 관련된 jobOpeningKeywordList를 조회
            List<JobOpening> jobOpenings = jobOpeningRepository.findAllWithJobOpeningKeywords();  // Find with the appropriate ID or query parameter
            log.info("총 {}개의 JobOpening 데이터를 조회했습니다.", jobOpenings.size());

            List<JobOpeningDocument> jobOpeningDocuments = new ArrayList<>();

            for (JobOpening jobOpening : jobOpenings) {
                // JobOpening에서 키워드 리스트 추출
                List<String> requiredSkills = new ArrayList<>();
                for (JobOpeningKeyword jobOpeningKeyword : jobOpening.getJobOpeningKeywordList()) {
                    Keyword keyword = jobOpeningKeyword.getKeyword();
                    if (keyword != null) {
                        requiredSkills.add(keyword.getName());
                    }
                }

                // JobOpeningDocument 생성
                JobOpeningDocument jobOpeningDocument = JobOpeningDocument.create(jobOpening);
                jobOpeningDocument.getRequiredSkills().addAll(requiredSkills);
                jobOpeningDocuments.add(jobOpeningDocument);
            }

            // Elasticsearch에 저장
            jobOpeningDocumentRepository.saveAll(jobOpeningDocuments);
            log.info("Mysql 데이터가 Elasticsearch에 성공적으로 동기화되었습니다");

        } catch (Exception e) {
            log.error("데이터 동기화 중 오류 발생", e);
        }
    }
}
