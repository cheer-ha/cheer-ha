package com.project.cheerha.domain.notification.scheduler;

import com.project.cheerha.common.util.SchedulerLockUtil;
import com.project.cheerha.domain.notification.dto.NotificationRecipientDto;
import com.project.cheerha.domain.notification.repository.NotificationDataProviderQuery;
import com.project.cheerha.domain.notification.service.NotificationService;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class NotificationDataFetchingScheduler {

    private final SchedulerLockUtil schedulerLockUtil;
    private final NotificationService notificationService;
    private final NotificationDataProviderQuery notificationDataProviderQuery;

    // 주기적으로 알림 생성용 데이터 조회
    @Scheduled(cron = "*/30 * * * * *")
    @Transactional
    public void fetchNotificationData() {
        schedulerLockUtil.lock("fetch_notification_data_lock");
        // 조회 시간을 30초 전으로 설정
        ZonedDateTime referenceTime = ZonedDateTime.now().minusSeconds(30L).withZoneSameInstant(ZoneId.of("UTC"));

        // key: 키워드 ID, value: 채용 공고 URL 목록
        Map<Long, List<String>> keywordIdToUrlList = notificationDataProviderQuery.findKeywordIdToUrlList(referenceTime);

        // 알림 받을 사용자의 이메일과 키워드 ID를 포함한 NotificationRecipientDto 목록
        List<NotificationRecipientDto> notificationRecipientDtoList = notificationDataProviderQuery.findNotificationRecipientDtoList();

        // 조회한 알림 생성용 데이터를 전달
        notificationService.createNotification(notificationRecipientDtoList, keywordIdToUrlList);
    }
}