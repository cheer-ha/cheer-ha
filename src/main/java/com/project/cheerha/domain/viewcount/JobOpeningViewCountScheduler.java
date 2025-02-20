package com.project.cheerha.domain.viewcount;

import com.project.cheerha.domain.viewcount.repository.JobOpeningViewCountRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * - 일정 주기로 job_opening_view_count 테이블의 조회수를 job_opening 테이블로 동기화하는 역할을 한다.
 * - Spring의 @Scheduled 어노테이션을 사용하여 배치 작업을 주기적으로 실행한다.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JobOpeningViewCountScheduler {

    /**
     * 조회수 정보를 관리하는 Repository와 채용공고 정보를 관리하는 Repository
     */
    private final JobOpeningViewCountRepository jobOpeningViewsRepository;
    private final JobOpeningRepository jobOpeningRepository;

    /**
     * 조회수 동기화 스케줄러
     * 1분마다 실행되어 job_opening_view_count 테이블의 viewCount를 job_opening 테이블의 viewCount로 동기화
     * fixedRate를 사용하여 3,600,000ms (1시간)마다 실행
     * 트랜잭션 어노테이션을 사용하여 트랜잭션 단위로 실행하여 정합성을 유지
     */
    @Scheduled(fixedRate = 3600000) // 1분마다 실행
    @Transactional
    public void syncViewCounts() {
        // 1. jobOpeningViewCount 테이블에서 존재하는 jobOpening.id 목록 가져오기
        List<Long> jobOpeningIds = jobOpeningViewsRepository.findAllJobOpeningId();

        // 2. 각 jobOpening.id별로 viewCount 조회 및 업데이트
        for (Long jobOpeningId : jobOpeningIds) {
            Long viewCount = jobOpeningViewsRepository.findViewCountByJobOpeningId(jobOpeningId);

            log.info("🔍 JobOpening ID: {}, ViewCount: {}", jobOpeningId, viewCount);

            if (viewCount != null && viewCount > 0) {
                jobOpeningRepository.updateViewCount(jobOpeningId, viewCount);

                log.info("✅ JobOpening ID {}: ViewCount {} 적용 완료", jobOpeningId, viewCount);

                jobOpeningViewsRepository.resetViewCount(jobOpeningId);

                log.info("🔄 JobOpeningViewCount 초기화 완료 - ID: {}", jobOpeningId);
            }
        }
        log.info("조회수 동기화 완료");
    }

}
