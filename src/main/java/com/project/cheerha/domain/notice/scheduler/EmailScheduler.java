package com.project.cheerha.domain.notice.scheduler;

import com.project.cheerha.domain.notice.UserDto;
import com.project.cheerha.domain.notice.service.EmailDataFetchService;
import com.project.cheerha.domain.notice.service.EmailService;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class EmailScheduler {

    private final EmailDataFetchService fetchService;
    private final EmailService emailService;
    private final ThreadPoolTaskScheduler threadPoolScheduler;

    // 아침 9시와 오후 5시에 실행
//    @Scheduled(cron = "0 0 9,17 * * *")
    @PostConstruct
    @Transactional
    public void sendJobOpeningMatchingNotices() {

        // 조회 시간: 30초 (UTC)
        // 데이터베이스에 저장된 시간과 비교하기 편하게 UTC로 변환
        ZonedDateTime referenceTime = ZonedDateTime.now().minusSeconds(30L).withZoneSameInstant(ZoneId.of("UTC"));

        // 조회 시간에 맞는 채용 공고 키워드 조회
        Map<Long, List<String>> jobOpeningKeywordMap = fetchService.findJobOpeningKeywordMap(referenceTime);

        // 사용자 키워드 조회 (이메일과 매칭할 사용자 정보)
        List<UserDto> userDtoList = fetchService.findUserKeywordList();

        // 사용자 이메일과 매칭된 채용 공고를 저장할 Map
        Map<String, Set<String>> emailUrlMap = new HashMap<>();

        // 사용자마다 키워드를 기준으로 매칭된 채용 공고 URL을 찾음
        for (UserDto dto : userDtoList) {
            // 해당 사용자의 키워드로 매칭되는 채용 공고 URL 목록 조회
            List<String> matchingUrlList = jobOpeningKeywordMap.getOrDefault(
                dto.keywordId(),
                List.of()
            );

            // 매칭된 채용 공고가 있으면 해당 사용자의 이메일에 URL 추가
            if (!matchingUrlList.isEmpty()) {
                emailUrlMap.computeIfAbsent(
                    dto.email(),
                    email -> new HashSet<>()
                ).addAll(matchingUrlList);
                // 해당 이메일에 매칭된 URL을 추가
            }
        }

        // 각 이메일로 매칭된 채용 공고를 비동기로 전송
        // SchedulerConfig에서 설정한 스레드 풀 10개 사용
        emailUrlMap.forEach((email, urlSet) -> {
            threadPoolScheduler.submit(() -> {
                log.info("매칭 결과: {} - {}", email, urlSet);
                emailService.sendMail(email, urlSet);
                // 매칭된 채용 공고 URL과 함께 해당 이메일로 전송
            });
        });
    }
}