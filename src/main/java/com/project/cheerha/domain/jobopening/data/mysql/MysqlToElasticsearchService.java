package com.project.cheerha.domain.jobopening.data.mysql;

import com.project.cheerha.domain.jobopening.entity.JobOpening;
import com.project.cheerha.domain.jobopening.entity.JobOpeningDocument;
import com.project.cheerha.domain.keyword.entity.JobOpeningKeyword;
import com.project.cheerha.domain.keyword.entity.Keyword;
import com.project.cheerha.domain.jobopening.elasticrepository.JobOpeningDocumentRepository;
import com.project.cheerha.domain.jobopening.repository.JobOpeningRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
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
    private JobOpeningDocumentRepository jobOpeningDocumentRepository;

    private boolean isFirstExecutionDone = false;  // 최초 실행 여부를 체크하는 변수

    /**
     * 최초 실행 후, 매일 자정에 실행되도록 하는 스케줄러입니다.
     * 이 메서드는 최초 1초 후에 실행되고, 이후 매일 자정(00:00)마다 주기적으로 실행됩니다.
     *
     * 최초 실행 시 데이터 동기화를 한 번 실행한 후, 이후에는 정해진 주기에 맞춰
     * MySQL 데이터를 Elasticsearch로 동기화하는 작업이 계속해서 실행됩니다.
     *
     * @Scheduled(cron = "0 0 0 * * *")
     * 최초 1회 실행되고, 이후 매일 자정에 반복적으로 실행됩니다.
     */
    @Scheduled(cron = "0 0 0 * * *")  // 매일 자정(00:00) 실행
    @Transactional
    public void syncDataToElasticsearch() {
        try {
            // 최초 실행 여부 확인
            if (!isFirstExecutionDone) {
                log.info("최초 한 번 실행되는 데이터 동기화 시작...");
                // 최초 실행 시 데이터 동기화
                syncDataOnce();
                isFirstExecutionDone = true;  // 최초 실행 완료 플래그 설정
            } else {
                log.info("주기적으로 실행되는 데이터 동기화 시작...");
                // 이후 매일 자정에 주기적으로 실행
                syncDataOnce();
            }
        } catch (Exception e) {
            log.error("데이터 동기화 중 오류 발생", e);
        }
    }

    /**
     * MySQL에서 채용 공고 데이터를 조회하여 Elasticsearch에 동기화하는 메서드입니다.
     *
     * 이 메서드는 MySQL에서 채용 공고와 관련된 키워드를 포함한 데이터를 가져오고,
     * 이를 Elasticsearch 형식에 맞게 변환하여 저장합니다.
     *
     * 1. MySQL에서 모든 채용 공고 데이터를 조회합니다.
     * 2. 각 채용 공고에 대해 관련된 키워드를 추출하여 `requiredSkills` 리스트에 추가합니다.
     * 3. `JobOpeningDocument` 객체로 변환하고 Elasticsearch에 저장합니다.
     *
     * @throws Exception 데이터 동기화 중 오류가 발생할 경우 예외를 발생시킵니다.
     */
    private void syncDataOnce() {
        try {
            log.info("MySQL에서 데이터를 조회하는 중...");
            // JobOpening과 관련된 키워드를 조회
            List<JobOpening> jobOpenings = jobOpeningRepository.findAllWithJobOpeningKeywords();  // 적절한 ID 또는 쿼리 매개변수를 사용하여 조회
            log.info("총 {}개의 JobOpening 데이터를 조회했습니다.", jobOpenings.size());

            List<JobOpeningDocument> jobOpeningDocuments = new ArrayList<>();

            // 각 JobOpening에 대해 키워드 리스트를 추출하여 requiredSkills에 추가
            for (JobOpening jobOpening : jobOpenings) {
                List<String> requiredSkills = new ArrayList<>();
                for (JobOpeningKeyword jobOpeningKeyword : jobOpening.getJobOpeningKeywordList()) {
                    Keyword keyword = jobOpeningKeyword.getKeyword();
                    if (keyword != null) {
                        requiredSkills.add(keyword.getName());
                    }
                }

                // JobOpening을 JobOpeningDocument로 변환
                JobOpeningDocument jobOpeningDocument = JobOpeningDocument.create(jobOpening);
                jobOpeningDocument.getRequiredSkills().addAll(requiredSkills);
                jobOpeningDocuments.add(jobOpeningDocument);
            }

            // Elasticsearch에 데이터 저장
            jobOpeningDocumentRepository.saveAll(jobOpeningDocuments);
            log.info("Mysql 데이터가 Elasticsearch에 성공적으로 동기화되었습니다");

        } catch (Exception e) {
            log.error("데이터 동기화 중 오류 발생", e);
        }
    }
}
