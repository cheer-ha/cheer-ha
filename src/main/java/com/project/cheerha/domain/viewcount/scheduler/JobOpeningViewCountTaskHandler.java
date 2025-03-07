package com.project.cheerha.domain.viewcount.scheduler;

import com.project.cheerha.common.scheduler.TaskHandler;
import com.project.cheerha.domain.jobopening.repository.JobOpeningRepository;
import com.project.cheerha.domain.viewcount.repository.JobOpeningViewCountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class JobOpeningViewCountTaskHandler implements TaskHandler {

    private final JobOpeningViewCountRepository jobOpeningViewCountRepository;
    private final JobOpeningRepository jobOpeningRepository;

    @Override
    public String getTaskType() {
        return "syncJobOpeningViewCount";
    }

    @Override
    @Transactional
    public void handle(Map<String, Object> payload) {
        List<Long> jobOpeningIdList = jobOpeningViewCountRepository.findDistinctViewedJobOpeningIdList();
        for (Long jobOpeningId : jobOpeningIdList) {
            Long viewCount = jobOpeningViewCountRepository.getViewCountByJobOpeningId(jobOpeningId);
            log.info("🔍 JobOpening ID: {}, ViewCount: {}", jobOpeningId, viewCount);
            if (viewCount != null && viewCount > 0) {
                jobOpeningRepository.updateViewCount(jobOpeningId, viewCount);
                log.info("✅ JobOpening ID {}: ViewCount {} 적용 완료", jobOpeningId, viewCount);
                jobOpeningViewCountRepository.resetViewCountByJobOpeningId(jobOpeningId);
                log.info("🔄 JobOpeningViewCount 초기화 완료 - ID: {}", jobOpeningId);
            }
        }
        log.info("조회수 동기화 완료");
    }

    @Override
    public long getScheduleIntervalMillis() {
        return 3600000L; // 1시간
    }

    @Override
    public Map<String, Object> getDefaultPayload() {
        return null;
    }
}
