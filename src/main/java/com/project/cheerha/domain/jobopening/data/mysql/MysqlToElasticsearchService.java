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

    /**
     * 초기화 메서드로, MySQL에서 Elasticsearch로 데이터를 동기화하는 작업을 시작합니다.
     * 이 메서드는 클래스가 초기화될 때 자동으로 호출됩니다.
     * `@PostConstruct` 어노테이션을 사용하여 객체가 생성된 후 초기화 작업을 수행합니다.
     */
    @PostConstruct
    public void init() {
        syncDataToElasticsearch();  // MySQL에서 Elasticsearch로 동기화하는 메서드를 초기화 시점에 호출
    }

    /**
     * MySQL에서 데이터를 조회하여 Elasticsearch에 동기화하는 메서드입니다.
     *
     * 이 메서드는 MySQL 데이터베이스에서 채용공고 데이터(JobOpening)와 그에 관련된 키워드 리스트(JobOpeningKeyword)를 조회하여,
     * 이를 Elasticsearch에 저장하는 작업을 수행합니다.
     *
     * 1. `jobOpeningRepository`를 사용하여 MySQL에서 모든 채용공고 데이터를 조회합니다.
     * 2. 각 채용공고에 관련된 키워드를 가져와 `requiredSkills` 리스트에 추가합니다.
     * 3. `JobOpeningDocument` 객체로 변환하여 Elasticsearch에 저장합니다.
     *
     * @throws Exception 데이터 동기화 중 오류가 발생한 경우 예외가 발생할 수 있습니다.
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
