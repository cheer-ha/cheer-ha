package com.project.cheerha.domain.jobopening.service;

import com.project.cheerha.common.annotation.ScheduledDynamic;
import com.project.cheerha.common.util.InstanceUtil;
import com.project.cheerha.domain.jobopening.entity.JobOpening;
import com.project.cheerha.domain.jobopening.repository.JobOpeningRepository;
import com.project.cheerha.domain.keyword.entity.JobOpeningKeyword;
import com.project.cheerha.domain.keyword.repository.JobOpeningKeywordRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@RequiredArgsConstructor
public class JobOpeningInsertScheduler {

    private final JobOpeningRepository jobOpeningRepository;
    private final JobOpeningKeywordRepository jobOpeningKeywordRepository;
    private final RedisTemplate<String, String> redisTemplate;
    private final Random random = new Random();

    private static final int JOB_COUNT = 3; //한 번에 생성할 최대 jobOpening 개수
    private static final String LOCK_KEY = "insert_job_opening_lock";
    private static final String LOCK_VALUE = InstanceUtil.getInstanceId();
    private static final long LOCK_TTL = 3; //3분간 유지

    /**
     * minMinutes - maxMinutes 사이의 랜덤한 시간 간격을 두고 랜덤한 채용공고를 삽입합니다.
     * 한 번에 최소 1 - 최대 JOB_COUNT 개의 데이터를 생성합니다.
     */
    @ScheduledDynamic(minMinutes = 1, maxMinutes = 2)
    public void insertRandomJobOpening() {
        //setIfAbsent 로 원자적으로 락을 잡으려 시도
        Boolean acquired = redisTemplate
                .opsForValue()
                .setIfAbsent(LOCK_KEY, LOCK_VALUE, LOCK_TTL, TimeUnit.MINUTES);

        //락을 못 잡았다면 내 인스턴스의 락인지 확인
        if (Boolean.FALSE.equals(acquired)) {
            String currentValue = redisTemplate.opsForValue().get(LOCK_KEY);
            //다른 인스턴스의 소유라면 그냥 리턴
            if (!LOCK_VALUE.equals(currentValue)) {
                log.info("다른 인스턴스에서 실행중인 스케줄러입니다.");
                return;
            }
            //만약 내 인스턴스라면 이전 스케줄이 남긴 락이거나
            //ttl 이 안 끝난 상황일 수 있음. ttl 재갱신
            redisTemplate.expire(LOCK_KEY, LOCK_TTL, TimeUnit.MINUTES);
        }

        log.info("랜덤 채용공고 데이터 삽입 시작");

        int jobCount = random.nextInt(JOB_COUNT) + 1;
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
