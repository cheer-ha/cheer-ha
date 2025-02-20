package com.project.cheerha.domain.jobopening.service;

import com.project.cheerha.common.annotation.ScheduledDynamic;
import com.project.cheerha.domain.jobopening.entity.JobOpening;
import com.project.cheerha.domain.jobopening.repository.JobOpeningRepository;
import com.project.cheerha.domain.keyword.entity.JobOpeningKeyword;
import com.project.cheerha.domain.keyword.repository.JobOpeningKeywordRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import java.util.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class JobOpeningInsertScheduler {

    private final JobOpeningRepository jobOpeningRepository;
    private final JobOpeningKeywordRepository jobOpeningKeywordRepository;
    private final Random random = new Random();

    @ScheduledDynamic(minMinutes = 1, maxMinutes = 3)
    public void insertRandomJobOpening() {
        log.info("랜덤 채용공고 데이터 삽입 시작");

        int jobCount = random.nextInt(3) + 1;
        log.info("{}개의 채용 공고 삽입 예정", jobCount);

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
    }
}
