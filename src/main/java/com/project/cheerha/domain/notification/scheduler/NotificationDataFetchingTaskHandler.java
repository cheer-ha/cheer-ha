package com.project.cheerha.domain.notification.scheduler;

import com.project.cheerha.common.scheduler.core.TaskHandler;
import com.project.cheerha.domain.notification.dto.NotificationDto;
import com.project.cheerha.domain.notification.repository.NotificationRepositoryQuery;
import com.project.cheerha.domain.notification.service.NotificationService;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationDataFetchingTaskHandler implements TaskHandler{

    private final NotificationService notificationService;
    private final NotificationRepositoryQuery notificationRepositoryQueryImpl;

    @Override
    public String getTaskType() {
        return "fetchNotificationData";
    }

    @Override
    public void handle(Map<String, Object> payload) {
        ZonedDateTime referenceTime = ZonedDateTime.now()
            .minusDays(1L)
            .withZoneSameInstant(ZoneId.of("UTC"));

        List<NotificationDto> notificationDtoList = notificationRepositoryQueryImpl
            .findTopMatchingJobOpeningsWithUsers(referenceTime);

        notificationService.createNotification(notificationDtoList);
    }

    @Override
    public long getScheduleIntervalMillis() {
        return 3600000L; //1시간
    }
}