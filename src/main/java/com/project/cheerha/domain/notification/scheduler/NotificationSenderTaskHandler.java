package com.project.cheerha.domain.notification.scheduler;

import com.project.cheerha.common.scheduler.core.TaskHandler;
import com.project.cheerha.common.email.sender.NotificationEmailSender;
import com.project.cheerha.common.scheduler.strategy.ScheduleStrategy;
import com.project.cheerha.common.scheduler.strategy.SpecificTimeStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationSenderTaskHandler implements TaskHandler {

    private final NotificationEmailSender notificationEmailSender;

    @Override
    public String getTaskType() {
        return "sendNotificationEmail";
    }

    // 매일 00시 30분에 전송
    @Override
    public void handle(Map<String, Object> payload) {
        log.info("이메일 알림 전송 시작");
        notificationEmailSender.sendNotificationEmails();
    }

    @Override
    public ScheduleStrategy getScheduleStrategy() {
        return new SpecificTimeStrategy(LocalTime.of(0, 30, 0));
    }
}
