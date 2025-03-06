package com.project.cheerha.domain.jobopening.schdeuler;

import com.project.cheerha.common.annotation.ScheduledDynamic;
import com.project.cheerha.common.exception.server.IllegalStatusException;
import com.project.cheerha.common.util.SchedulerLockUtil;
import com.project.cheerha.domain.jobopening.entity.JobOpening;
import com.project.cheerha.domain.jobopening.repository.JobOpeningRepository;
import com.project.cheerha.domain.jobopening.schdeuler.factory.JobOpeningFactory;
import com.project.cheerha.domain.jobopening.schdeuler.factory.JobOpeningKeywordFactory;
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
    private final SchedulerLockUtil schedulerLockUtil;
    private final Random random = new Random();

    private static final int JOB_COUNT = 3; //한 번에 생성할 최대 jobOpening 개수
    private static final String LOCK_KEY = "insert_job_opening_lock";
    private static final long LOCK_TTL = 4; //3분간 유지(minMinutes + 1)

    /**
     * minMinutes - maxMinutes 사이의 랜덤한 시간 간격을 두고 랜덤한 채용공고를 삽입합니다.
     * 한 번에 최소 1 - 최대 JOB_COUNT 개의 데이터를 생성합니다.
     */
    @ScheduledDynamic(minMinutes = 3, maxMinutes = 40)
    public void insertRandomJobOpening() {
        try {
            schedulerLockUtil.lock(LOCK_KEY, LOCK_TTL);

            log.info("랜덤 채용공고 데이터 삽입 시작");

            int jobCount = random.nextInt(JOB_COUNT) + 1;
            log.info("{}개의 채용 공고 삽입 예정", jobCount);

            List<JobOpening> jobOpenings = new ArrayList<>();
            List<JobOpeningKeyword> allJobOpeningKeywords = new ArrayList<>();

            for (int i = 0; i < jobCount; i++) {
                JobOpening jobOpening = JobOpeningFactory.createRandomJobOpening();
                jobOpenings.add(jobOpening);

                List<JobOpeningKeyword> jobOpeningKeywords =
                        JobOpeningKeywordFactory.createRandomKeywordList(jobOpening);
                allJobOpeningKeywords.addAll(jobOpeningKeywords);
            }

            jobOpeningRepository.saveAll(jobOpenings);
            jobOpeningKeywordRepository.saveAll(allJobOpeningKeywords);

            log.info("총 {}개의 채용 공고가 삽입되었습니다.", jobOpenings.size());
        } catch (IllegalStatusException e) {
            log.warn("현재 인스턴스의 스케줄러를 종료합니다.");
        } catch (Exception e) {
            log.error("채용공고 삽입 중 예상치 못한 예외 발생", e);
        }
    }
}
