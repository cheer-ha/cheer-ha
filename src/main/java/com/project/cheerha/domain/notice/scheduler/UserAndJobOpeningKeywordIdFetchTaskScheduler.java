package com.project.cheerha.domain.notice.scheduler;

import com.project.cheerha.domain.notice.dto.UserDto;
import com.project.cheerha.domain.notice.service.UserAndJobOpeningKeywordFetchService;
import com.project.cheerha.domain.notice.service.MappingService;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserAndJobOpeningKeywordIdFetchTaskScheduler {

    private final UserAndJobOpeningKeywordFetchService userAndJobOpeningKeywordFetchService;
    private final MappingService mappingService;

    @Scheduled(cron = "*/30 * * * * *")
    @Transactional
    public void fetchUserAndJobOpeningKeywordIds() {
        ZonedDateTime referenceTime = ZonedDateTime.now()
            .minusSeconds(30L)
            .withZoneSameInstant(ZoneId.of("UTC"));

        Map<Long, List<String>> keywordIdToUrlList =
            userAndJobOpeningKeywordFetchService.findKeywordIdToUrlList(referenceTime);

        List<UserDto> userDtoList = userAndJobOpeningKeywordFetchService
            .findUserDtoList();

        mappingService.saveMappings(
            userDtoList,
            keywordIdToUrlList
        );
    }
}