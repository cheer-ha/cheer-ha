package com.project.cheerha.domain.viewcount.service;

import com.project.cheerha.common.redis.RedisDistributedLockManager;
import com.project.cheerha.common.redis.RedisViewCountManager;
import com.project.cheerha.domain.jobopening.entity.JobOpening;
import com.project.cheerha.domain.jobopening.service.JobOpeningFindByService;
import com.project.cheerha.domain.viewcount.entity.JobOpeningViewCount;
import com.project.cheerha.domain.viewcount.repository.JobOpeningViewCountRepository;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class JobOpeningViewCountService {

    private final RedisViewCountManager redisViewCountManager;
    private final RedisDistributedLockManager redisDistributedLockManager;
    private final JobOpeningViewCountRepository jobOpeningViewCountRepository;
    private final JobOpeningFindByService jobOpeningFindByService;

    /**
     * 채용공고 리다이렉트 동시성 제어를 위한 레디스 조회수 카운팅 메서드 입니다. viewCount 정보를 관리하는 레디스에서 조회수가 카운팅됩니다.
     * 레디스와 분산 락이 적용됩니다.
     */

    public String increaseViewCount(Long jobOpeningId) {
        redisViewCountManager.increaseViewCount(jobOpeningId);
        String result = updateViewCount(jobOpeningId);
        return result;
    }

    /**
     * 레디스에 있는 채용공고 조회수를 집계테이블로 업데이트 하는데 사용하는 메서드
     * @param jobOpeningId
     */
    @Transactional
    public String updateViewCount(Long jobOpeningId) {
        String lockKey = "JOB_OPENING_VIEW_COUNT_" + jobOpeningId;
        int retryCount = 3; // 최대 3번 재시도
        boolean success = false;

        while (retryCount-- > 0) {
            success = redisDistributedLockManager.tryLockAndRun(
                lockKey,
                10,
                5,
                TimeUnit.SECONDS,
                () -> {
                    // 실제 처리할 비즈니스 로직
                    Long redisviewCount = redisViewCountManager.getViewCount(jobOpeningId);
                    JobOpeningViewCount jobOpeningViewCount = findByJobOpeningViewCountInJobOpeningId(jobOpeningId);
                    jobOpeningViewCount.increaseViewCount(redisviewCount);
                });

            if (success) {
                // 락 획득에 성공한 케이스
                log.info("JobOpeningId: {} 반영조회수: {} 락 획득 로직 실행 완료", jobOpeningId , redisViewCountManager.getViewCount(jobOpeningId));
                return "success";
            }

            log.warn("JobOpeningId: {} 락 획득 실패", jobOpeningId);
            try {
                Thread.sleep(100);// 1초 대기
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        log.error("JobOpeningId: {} 조회수 동기화 최종 실패 (모든 재시도 실패)", jobOpeningId);
        return "fail";
    }

    private JobOpeningViewCount findByJobOpeningViewCountInJobOpeningId(Long jobOpeningId) {
        JobOpeningViewCount jobOpeningViewCount = jobOpeningViewCountRepository.findByJobOpeningId(
                jobOpeningId)
            .orElseGet(() -> {
                JobOpening jobOpening = jobOpeningFindByService.findById(jobOpeningId);
                return jobOpeningViewCountRepository.save(
                    JobOpeningViewCount.create(jobOpening));
            });
        return jobOpeningViewCount;
    }
}

