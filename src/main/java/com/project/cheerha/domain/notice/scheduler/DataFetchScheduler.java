package com.project.cheerha.domain.notice.scheduler;

import com.project.cheerha.domain.notice.UserDto;
import com.project.cheerha.domain.notice.service.DataFetchService;
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
public class DataFetchScheduler {

    private final DataFetchService dataFetchService;
    private final MappingService mappingService;

    @Scheduled(cron = "*/30 * * * * *")
    @Transactional
    public void fetchData() {
        ZonedDateTime referenceTime = ZonedDateTime.now()
            .minusDays(7L)
            .withZoneSameInstant(ZoneId.of("UTC"));

        Map<Long, List<String>> keywordIdToUrlList =
            dataFetchService.findKeywordIdToUrlList(referenceTime);

        List<UserDto> userDtoList = dataFetchService
            .findUserDtoList();

        mappingService.saveMappings(
            userDtoList,
            keywordIdToUrlList
        );
    }
}