package com.project.cheerha.domain.jobopening.schdeuler;

import com.project.cheerha.common.dto.ViewCountResponseDto;
import com.project.cheerha.domain.jobopening.service.ViewCountManager;
import com.project.cheerha.common.scheduler.core.TaskHandler;
import com.project.cheerha.domain.jobopening.repository.JobOpeningRepository;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class JobOpeningViewCountTaskHandler implements TaskHandler {

    private final JobOpeningRepository jobOpeningRepository;
    private final ViewCountManager viewCountManager;

    @Override
    public String getTaskType() {
        return "syncJobOpeningViewCount";
    }

    @Override
    @Transactional
    public void handle(Map<String, Object> payload) {
        List<ViewCountResponseDto> redisJobOpeningViewCountList = viewCountManager.findAllViewCount();
        for (ViewCountResponseDto jobOpening : redisJobOpeningViewCountList) {
            Long jobOpeningId = jobOpening.key();
            Long viewCount = jobOpening.Value();
            if (viewCount != null && viewCount > 0) {
                jobOpeningRepository.updateViewCount(jobOpeningId, viewCount);
                viewCountManager.resetViewCount(jobOpeningId);
            }
        }
        log.info("조회수 동기화 완료");
    }

    @Override
    public long getScheduleIntervalMillis() {
        return 3600000L; // 1시간
//        return 60000L; //5분
    }
}
