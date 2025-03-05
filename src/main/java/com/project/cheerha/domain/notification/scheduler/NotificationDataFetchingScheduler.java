package com.project.cheerha.domain.notification.scheduler;

import com.project.cheerha.domain.notification.dto.NotificationRecipientDto;
import com.project.cheerha.domain.notification.repository.NotificationDataRepositoryQuery;
import com.project.cheerha.domain.notification.service.NotificationService;
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
public class NotificationDataFetchingScheduler {

    private final NotificationService notificationService;
    private final NotificationDataRepositoryQuery notificationDataRepositoryQuery;

    @Scheduled(cron = "*/30 * * * * *")
    @Transactional
    public void fetchNotificationData() {
        ZonedDateTime referenceTime = ZonedDateTime.now().minusSeconds(30L).withZoneSameInstant(ZoneId.of("UTC"));

        Map<Long, List<String>> keywordIdToUrlList = notificationDataRepositoryQuery.findAllJobOpeningKeywords(referenceTime);

        List<NotificationRecipientDto> notificationRecipientDtoList = notificationDataRepositoryQuery.findAllUserKeywords();

        notificationService.createNotification(notificationRecipientDtoList, keywordIdToUrlList);
    }
}