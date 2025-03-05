package com.project.cheerha.domain.notification.scheduler;

import com.project.cheerha.common.email.sender.NotificationEmailSender;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SenderScheduler {

    private final NotificationEmailSender notificationEmailSender;

    // Notification 생성 작업 완료 후 60초 간격으로 다시 실행
    @Scheduled(fixedDelay = 60_000)
    public void sendEmailNotification() {
        log.info("이메일 알림 전송 시작");

        notificationEmailSender.sendNotificationEmails();
    }
}