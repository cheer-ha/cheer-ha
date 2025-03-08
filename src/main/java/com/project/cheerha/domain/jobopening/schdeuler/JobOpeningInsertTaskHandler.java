package com.project.cheerha.domain.jobopening.schdeuler;

import com.project.cheerha.common.scheduler.TaskHandler;
import com.project.cheerha.domain.jobopening.entity.JobOpening;
import com.project.cheerha.domain.jobopening.repository.JobOpeningRepository;
import com.project.cheerha.domain.jobopening.schdeuler.factory.JobOpeningFactory;
import com.project.cheerha.domain.jobopening.schdeuler.factory.JobOpeningKeywordFactory;
import com.project.cheerha.domain.keyword.entity.JobOpeningKeyword;
import com.project.cheerha.domain.keyword.repository.JobOpeningKeywordRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class JobOpeningInsertTaskHandler implements TaskHandler {

    private final JobOpeningRepository jobOpeningRepository;
    private final JobOpeningKeywordRepository jobOpeningKeywordRepository;

    @Override
    public String getTaskType() {
        return "insertRandomJobOpening";
    }

    @Override
    public void handle(Map<String, Object> payload) {
        try {
            int jobCount = ((Number) payload.get("jobCount")).intValue();
            log.info("{}개의 채용 공고 삽입 시작", jobCount);

            List<JobOpening> jobOpenings = new ArrayList<>();
            List<JobOpeningKeyword> allJobOpeningKeywords = new ArrayList<>();

            for (int i = 0; i < jobCount; i++) {
                JobOpening jobOpening = JobOpeningFactory.createRandomJobOpening();
                jobOpenings.add(jobOpening);

                List<JobOpeningKeyword> jobOpeningKeywords = JobOpeningKeywordFactory.createRandomKeywordList(jobOpening);
                allJobOpeningKeywords.addAll(jobOpeningKeywords);
            }

            jobOpeningRepository.saveAll(jobOpenings);
            jobOpeningKeywordRepository.saveAll(allJobOpeningKeywords);

            log.info("총 {}개의 채용 공고가 삽입되었습니다.", jobOpenings.size());
        } catch (Exception e) {
            log.error("채용공고 삽입 중 예외 발생", e);
            throw new RuntimeException("Job opening insertion failed", e);
        }
    }

    @Override
    public long getScheduleIntervalMillis() {
        return 2400000L;    //40분
    }

    @Override
    public Map<String, Object> getDefaultPayload() {
        Map<String, Object> payload = new HashMap<>();
        payload.put("jobCount", 3);  //기본적으로 3개의 채용 공고 삽입
        return payload;
    }
}
