package com.project.cheerha.domain.notice.scheduler.refactor;

import com.project.cheerha.domain.notice.dto.refactor.UserKeywordDto;
import com.project.cheerha.domain.notice.service.EmailService;
import com.project.cheerha.domain.notice.service.refactor.EmailDataFetchService;
import java.time.ZoneId;
import java.time.ZonedDateTime;
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
public class EmailScheduler {

    private final EmailDataFetchService emailDataFetchService;
    private final EmailService emailService;

    // 30초마다 채용 공고를 조회하여 이메일을 전송하는 스케줄러
    @Scheduled(cron = "*/30 * * * * *")
    @Transactional
    public void sendJobOpeningMatchingNotices() {
        // 중복 조회를 방지하고자 조회 시간 사용
        ZonedDateTime referenceTime = ZonedDateTime.now().minusDays(2L)
            .withZoneSameInstant(ZoneId.of("UTC"));

        // Map<KeywordId, List<JobOpeningUrl>> 형태로 채용 공고 조회
        Map<Long, List<String>> jobOpeningKeywordMap = emailDataFetchService.findJobOpeningKeywordMap(referenceTime);

        // 사용자의 ID, 키워드 ID, 이메일 조회
        List<UserKeywordDto> userKeywordDtoList = emailDataFetchService.findUserKeywordList();

        // 사용자의 이메일 및 연결된 채용 공고 URL을 저장할 Map
        Map<String, Set<String>> emailUrlMap = new HashMap<>();

        for (UserKeywordDto dto : userKeywordDtoList) {
            // 해당 사용자의 키워드로 채용 공고 URL을 가져옴
            List<String> matchingUrlList = jobOpeningKeywordMap.getOrDefault(
                dto.keywordId(),
                List.of()
            );

            if (!matchingUrlList.isEmpty()) {
                // 사용자의 이메일을 Key로 공고 URL을 Set에 저장 (중복 제거)
                emailUrlMap
                    .computeIfAbsent(
                        dto.email(),
                        emailAsKey -> new HashSet<>()
                    ).addAll(matchingUrlList
                    );
            }

            // 이메일별로 알림 전송
            emailUrlMap.forEach((email, urlSet) -> {
                emailService.sendMail(email, List.copyOf(urlSet));
            });
        }
    }
}