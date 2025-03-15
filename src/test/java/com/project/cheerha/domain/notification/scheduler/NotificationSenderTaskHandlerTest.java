//package com.project.cheerha.domain.notification.scheduler;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.mockito.Mockito.times;
//import static org.mockito.Mockito.verify;
//
//import com.project.cheerha.common.email.sender.NotificationEmailSender;
//import com.project.cheerha.common.scheduler.strategy.ScheduleStrategy;
//import com.project.cheerha.common.scheduler.strategy.SpecificTimeStrategy;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//@ExtendWith(MockitoExtension.class)
//class NotificationSenderTaskHandlerTest {
//
//    @Mock
//    private NotificationEmailSender notificationEmailSender;
//
//    @Test
//    @DisplayName("성공 - getTaskType 메서드가 'sendNotificationEmail' 반환")
//    void succeedsToGetTaskType() {
//
//        // then
//        String expectedTaskType = "sendNotificationEmail";
//
//        assertThat(actualTaskType).isEqualTo(expectedTaskType);
//    }
//
//    @Test
//    @DisplayName("성공 - handle 메서드가 sendNotificationEmails 호출")
//    void succeedsToInvokeSendNotificationEmails() {
//
//        // then
//        // sendNotificationEmails() 메서드가 1번 호출되었는지 확인
//        verify(notificationEmailSender, times(1)).sendNotificationEmails();
//    }
//
//    @Test
//    @DisplayName("성공 - getScheduleStrategy 메서드가 SpecificTimeStrategy 반환")
//    void succeedsToGetScheduleStrategy() {
//        // when
//        ScheduleStrategy strategy = notificationSenderTaskHandler.getScheduleStrategy();
//
//        // then
//        // 반환된 ScheduleStrategy가 SpecificTimeStrategy 클래스의 인스턴스인지 검증
//        assertThat(strategy.getClass()).isEqualTo(SpecificTimeStrategy.class);
//    }
//}