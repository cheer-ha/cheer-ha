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

    @Override
    public void handle(Map<String, Object> payload) {
        //30초 전 기준으로 데이터 조회 (UTC 기준)
        ZonedDateTime referenceTime = ZonedDateTime.now().minusSeconds(30L).withZoneSameInstant(ZoneId.of("UTC"));
        Map<Long, List<String>> keywordIdToUrlList = notificationDataProviderQuery.findKeywordIdToUrlList(referenceTime);
        List<NotificationRecipientDto> notificationRecipientDtoList = notificationDataProviderQuery.findNotificationRecipientDtoList();
        notificationService.createNotification(notificationRecipientDtoList, keywordIdToUrlList);
    }

    @Override
    public long getScheduleIntervalMillis() {
        return 30000L; //30초
    }

    @Override
    public Map<String, Object> getDefaultPayload() {
        return null;
    }
}
