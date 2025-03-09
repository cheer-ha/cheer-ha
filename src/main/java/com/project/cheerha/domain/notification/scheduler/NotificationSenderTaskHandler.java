package com.project.cheerha.domain.notification.scheduler;

import com.project.cheerha.common.scheduler.TaskHandler;
import com.project.cheerha.common.email.sender.NotificationEmailSender;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

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

    // Notification 생성 작업 완료 후 60초 간격으로 다시 실행
    @Override
    public void handle(Map<String, Object> payload) {
        log.info("이메일 알림 전송 시작");
        notificationEmailSender.sendNotificationEmails();
    }

    @Override
    public long getScheduleIntervalMillis() {
        return 60000L; //60초
    }
}
