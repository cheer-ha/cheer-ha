package com.project.cheerha.common.email.sender;

import com.project.cheerha.common.email.format.NotificationFormat;
import com.project.cheerha.domain.notification.entity.Notification;
import com.project.cheerha.domain.notification.repository.NotificationRepository;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationEmailSender {

    private final NotificationRepository notificationRepository;
    private final EmailSender emailSender;

    // Notification을 이메일로 비동기 전송
    @Async
    public void sendNotificationEmails() {
        // key: 알림 받을 이메일, value: Notification Set
        Map<String, Set<Notification>> emailToNotificationSet = new HashMap<>();

        // (1) 이메일로 전송되지 않은 Notification 조회
        // (2) 이메일 주소별로 해당 Notification Set 그룹화하여 저장
        notificationRepository.findByIsEmailSentFalse().forEach(
            notification -> emailToNotificationSet
                .computeIfAbsent(notification.getEmail(), emailAsKey -> new HashSet<>())
                .add(notification)
        );

        // 묶은 알림을 각 이메일로 전송
        emailToNotificationSet.forEach(this::sendNotificationEmail);
    }

    private void sendNotificationEmail(String recipientEmail, Set<Notification> notificationSet) {
        try {
            // 이메일 내용 생성
            String[] emailData = NotificationFormat.createEmailNotification(notificationSet);
            String subject = emailData[0];
            String content = emailData[1];

            // 이메일 전송
            emailSender.send(recipientEmail, subject, content);

            // 전송된 알림 상태 변경
            notificationSet.forEach(notification -> {
                notification.markEmailAsSent();
                notificationRepository.save(notification);
            });

        } catch (IOException e) {
            log.error("이메일 전송 실패: {}", recipientEmail, e);
        }
    }
}