package com.project.cheerha.domain.notice;

import com.project.cheerha.domain.notice.dto.JobOpeningKeywordDto;
import com.project.cheerha.domain.notice.dto.UserKeywordDto;
import com.project.cheerha.domain.notice.service.NoticeCreationService;
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

    private final NoticeCreationService noticeCreationService;

    @Scheduled(cron = "*/30 * * * * *")
    @Transactional
    public void sendJobOpeningMatchingNotices() {

        List<JobOpeningKeywordDto> jobOpeningKeywordDtoList = noticeCreationService.findAllJobOpeningKeywords();
        List<UserKeywordDto> userKeywordDtoList = noticeCreationService.findAllUserKeywords();

        log.info("채용 공고 목록 {}: ", userKeywordDtoList);
        log.info("사용자 목록 {}: ", jobOpeningKeywordDtoList);

        // 사용자의 이메일 및 연결된 채용 공고 URL을 저장할 Map
        Map<String, Set<String>> emailUrlMap = new HashMap<>();

        // (1) 사용자 키워드 ID 목록 순회
        for (UserKeywordDto userDto : userKeywordDtoList) {

            // (2) 모든 채용 공고 키워드 ID 목록 순회
            for (JobOpeningKeywordDto jobOpeningKeywordDto : jobOpeningKeywordDtoList) {

                // (3) 키워드끼리 일치하는지 확인
                boolean isUserKeywordMatchingJobKeyword = userDto.keywordId()
                    .equals(jobOpeningKeywordDto.keywordId());

                // (4) 일치하면 이메일과 채용 공고 URL을 Map에 저장
                if (isUserKeywordMatchingJobKeyword) {
                    emailUrlMap
                        .computeIfAbsent(userDto.email(), emailAsKey -> new HashSet<>())
                        .add(jobOpeningKeywordDto.url());
                }
            }
        }

        // 이메일별 알림 전송 준비
        emailUrlMap.forEach((email, urlSet) -> {
                log.info("이메일 전송 준비 완료: {} -> {}", email, urlSet);
            }
        );
    }
}