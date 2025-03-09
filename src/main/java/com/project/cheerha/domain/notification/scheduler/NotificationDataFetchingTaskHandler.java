package com.project.cheerha.domain.notification.scheduler;

import com.project.cheerha.common.scheduler.TaskHandler;
import com.project.cheerha.domain.notification.dto.NotificationRecipientDto;
import com.project.cheerha.domain.notification.repository.NotificationDataProviderQuery;
import com.project.cheerha.domain.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class NotificationDataFetchingTaskHandler implements TaskHandler {

    private final NotificationService notificationService;
    private final NotificationDataProviderQuery notificationDataProviderQuery;

    @Override
    public String getTaskType() {
        return "fetchNotificationData";
    }

    // 주기적으로 알림 생성용 데이터 조회
    @Override
    public void handle(Map<String, Object> payload) {
        //30초 전 기준으로 데이터 조회 (UTC 기준)
        ZonedDateTime referenceTime = ZonedDateTime.now().minusSeconds(30L).withZoneSameInstant(ZoneId.of("UTC"));
        // key: 키워드 ID, value: 채용 공고 URL 목록
        Map<Long, List<String>> keywordIdToUrlList = notificationDataProviderQuery.findKeywordIdToUrlList(referenceTime);
        // 알림 받을 사용자의 이메일과 키워드 ID를 포함한 NotificationRecipientDto 목록
        List<NotificationRecipientDto> notificationRecipientDtoList = notificationDataProviderQuery.findNotificationRecipientDtoList();
        // 조회한 알림 생성용 데이터를 전달
        notificationService.createNotification(notificationRecipientDtoList, keywordIdToUrlList);
    }

    @Override
    public long getScheduleIntervalMillis() {
        return 30000L; //30초
    }
}
