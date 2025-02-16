package com.project.cheerha.domain.notice;

import com.project.cheerha.domain.notice.dto.JobOpeningDto;
import com.project.cheerha.domain.notice.dto.JobOpeningKeywordDto;
import com.project.cheerha.domain.notice.dto.UserDto;
import com.project.cheerha.domain.notice.dto.UserKeywordDto;
import com.project.cheerha.domain.notice.service.NoticeCreationService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class NoticeCreationScheduler {

    private final NoticeCreationService service;

    @Scheduled(cron = "*/30 * * * * *")
    @Transactional
    public void sendJobOpeningMatchingNotices() {

        List<JobOpeningKeywordDto> jobOpeningKeywordDtoList = service.findAllJobOpeningKeywords();
        List<UserKeywordDto> userKeywordDtoList = service.findAllUserKeywords();
        List<UserDto> userDtoList = service.findAllUsers();
        List<JobOpeningDto> jobOpeningDtoList = service.findAllJobOpenings();

        // 사용자별로 선택한 키워드 ID 목록을 저장할 맵
        Map<Long, List<Long>> userKeywordMap = new HashMap<>();

        // userId를 키로 사용하여 userKeywordMap에 매핑하기
        for (UserKeywordDto dto : userKeywordDtoList) {
            userKeywordMap.computeIfAbsent(
                dto.userId(),
                keywordId -> new ArrayList<>()
            ).add(dto.keywordId());
        }

        log.info("사용자별 키워드 목록: {}", userKeywordMap);

        // 채용 공고별로 포함된 키워드 ID 목록을 저장할 맵
        Map<Long, List<Long>> jobOpeningKeywordMap = new HashMap<>();

        // jobOpeningId를 키로 사용하여 jobOpeningKeywordMap에 매핑하기
        for (JobOpeningKeywordDto dto : jobOpeningKeywordDtoList) {
            jobOpeningKeywordMap.computeIfAbsent(
                dto.jobOpeningId(),
                keywordId -> new ArrayList<>()
            ).add(dto.keywordId());
        }

        log.info("채용 공고별 키워드 목록: {}", jobOpeningKeywordMap);

        // 사용자별로 매칭된 채용 공고 ID 목록을 저장할 맵
        Map<Long, List<Long>> matchingMap = new HashMap<>();

        // 사용자가 고른 키워드 목록을 확인하고, 각 키워드와 매칭되는 채용 공고 찾기
        for (Map.Entry<Long, List<Long>> entry : userKeywordMap.entrySet()) {
            Long userId = entry.getKey(); // 사용자 ID
            Set<Long> keywordIdSetChosenByUser = new HashSet<>(entry.getValue());

            // 채용 공고에 포함된 키워드 목록과 사용자가 고른 키워드 목록을 비교하여 매칭된 채용 공고 찾기
            List<Long> jobOpeningIdList = jobOpeningKeywordMap.entrySet().stream()
                .filter(jobOpening -> jobOpening.getValue().stream()
                    .anyMatch(keywordIdSetChosenByUser::contains))
                .map(Map.Entry::getKey)
                .toList();

            // 매칭된 채용 공고가 존재한다면 해당 채용 공고의 ID를 사용자와 매핑
            if (!jobOpeningIdList.isEmpty()) {
                matchingMap.put(userId, jobOpeningIdList);

                log.info("{}번 사용자와 연결된 채용 공고: {}", userId, jobOpeningIdList);
            }
        }
    }
}