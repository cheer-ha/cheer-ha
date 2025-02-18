package com.project.cheerha.domain.notice;

import com.project.cheerha.domain.notice.dto.JobOpeningKeywordDto;
import com.project.cheerha.domain.notice.dto.UserKeywordDto;
import com.project.cheerha.domain.notice.service.EmailService;
import com.project.cheerha.domain.notice.service.NoticeCreationService;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class NoticeCreationScheduler {

    private final NoticeCreationService noticeCreationService;
    private final EmailService emailService;

    @Scheduled(cron = "*/30 * * * * *")
    @Transactional
    public void sendJobOpeningMatchingNotices() {

        ZonedDateTime referenceTime = ZonedDateTime.now().minusSeconds(30L)
            .withZoneSameInstant(ZoneId.of("UTC"));
        ;

        List<JobOpeningKeywordDto> jobOpeningKeywordDtoList = noticeCreationService.findAllJobOpeningKeywords(
            referenceTime);
        List<UserKeywordDto> userKeywordDtoList = noticeCreationService.findAllUserKeywords();

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
                        .computeIfAbsent(
                            userDto.email(),
                            emailAsKey -> new HashSet<>()
                        ).add(jobOpeningKeywordDto.url());
                }
            }
        }
        // (5) 이메일별로 알림 전송
        emailUrlMap.forEach((email, urlSet) -> {
                emailService.sendMail(email, List.copyOf(urlSet));
            }
        );
    }
}