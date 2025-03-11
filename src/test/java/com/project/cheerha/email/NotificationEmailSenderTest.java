package com.project.cheerha.email;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.project.cheerha.common.email.sender.EmailSender;
import com.project.cheerha.common.email.sender.NotificationEmailSender;
import com.project.cheerha.domain.notification.entity.Notification;
import com.project.cheerha.domain.notification.repository.NotificationRepository;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class NotificationEmailSenderTest {

    @Mock
    private NotificationRepository notificationRepository;

    @Mock
    private EmailSender emailSender;

    @InjectMocks
    private NotificationEmailSender notificationEmailSender;

    private final List<Notification> notificationList = new ArrayList<>();
    private final Notification notification = mock(Notification.class);

    @BeforeEach
    void setUp() {
        notificationList.add(notification);
    }

    @Test
    @DisplayName("성공 - 이메일 알림 전송")
    void succeedsToSendNotificationEmails() throws IOException {
        // given
        when(notificationRepository.findByIsEmailSentFalse()).thenReturn(notificationList);

        // when
        // 이메일 전송 메서드 실행
        notificationEmailSender.sendNotificationEmails();

        // then
        // 이메일 전송 메서드가 호출되었는지 확인
        // any() 3개: String recipientEmail, String subject, String content
        verify(emailSender).send(any(), any(), any());

        // 이메일 전송 후, Notification 객체의 이메일 발송 상태가 true로 변경되었는지 확인
        verify(notification).markEmailAsSent();

        // 이메일 전송 후, Notification 객체가 저장되었는지 확인
        verify(notificationRepository).save(notification);
    }

    @Test
    @DisplayName("실패 - 이메일 알림 전송 실패 시 IOException 발생")
    void failsToSendNotificationEmailDueToIOException() throws IOException {
        // given
        when(notificationRepository.findByIsEmailSentFalse()).thenReturn(notificationList);

        // 이메일 전송 과정에서 IOException이 발생한다고 설정
        doThrow(new IOException("예외 발생")).when(emailSender).send(any(), any(), any());

        // when
        // 이메일 전송 메서드 실행
        notificationEmailSender.sendNotificationEmails();

        // then
        // 이메일 전송 실패 후, Notification 객체의 이메일 발송 상태가 변경되지 않았는지 확인
        verify(notification, never()).markEmailAsSent();

        // 이메일 전송 실패 후, Notification 객체가 저장되지 않았는지 확인
        verify(notificationRepository, never()).save(any());
    }
}