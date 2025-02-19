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

    private final EmailDataFetchService fetchService;
    private final EmailService emailService;

    @Scheduled(cron = "*/30 * * * * *")
    @Transactional
    public void sendJobOpeningMatchingNotices() {
        ZonedDateTime referenceTime = ZonedDateTime.now()
            .minusDays(2L)
            .withZoneSameInstant(ZoneId.of("UTC"));

        Map<Long, List<String>> jobOpeningKeywordMap = fetchService
            .findJobOpeningKeywordMap(referenceTime);

        List<UserKeywordDto> userKeywordDtoList = fetchService
            .findUserKeywordList();

        Map<String, Set<String>> emailUrlMap = new HashMap<>();

        for (UserKeywordDto dto : userKeywordDtoList) {
            List<String> matchingUrlList = jobOpeningKeywordMap
                .getOrDefault(dto.keywordId(), List.of());

            boolean hasMatchingUrls = !matchingUrlList.isEmpty();

            if (hasMatchingUrls) {
                emailUrlMap
                    .computeIfAbsent(
                        dto.email(),
                        emailAsKey -> new HashSet<>()
                    ).addAll(matchingUrlList);
            }

            emailUrlMap.forEach((email, urlSet) -> {
                    emailService.sendMail(
                        email,
                        List.copyOf(urlSet)
                    );
                }
            );
        }
    }
}