package com.project.cheerha.domain.notification.scheduler;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.project.cheerha.domain.notification.dto.NotificationDto;
import com.project.cheerha.domain.notification.repository.NotificationRepositoryQuery;
import com.project.cheerha.domain.notification.service.NotificationService;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class NotificationDataFetchingTaskHandlerTest {

    @Mock
    private NotificationService notificationService;

    @Mock
    private NotificationRepositoryQuery notificationRepositoryQueryImpl;

    @InjectMocks
    private NotificationDataFetchingTaskHandler notificationDataFetchingTaskHandler;

    private final List<NotificationDto> notificationDtoList = Arrays.asList(
        new NotificationDto("user1@example.com", "https://job.com/1"),
        new NotificationDto("user2@example.com", "https://job/com/2")
    );

    @Test
    @DisplayName("성공 - 주기적으로 알림 생성용 데이터 조회")
    void succeedsToFetchDataForNotificationCreation() {
        // given
        when(notificationRepositoryQueryImpl.findTopMatchingJobOpeningsWithUsers(any(ZonedDateTime.class))).thenReturn(notificationDtoList);

        // when
        notificationDataFetchingTaskHandler.handle(Map.of());  // payload는 비어있어도 됨

        // then
        // findTopMatchingJobOpeningsWithUsers 메서드가 1번 호출되었는지 확인
        verify(notificationRepositoryQueryImpl, times(1)).findTopMatchingJobOpeningsWithUsers(any(ZonedDateTime.class));

        // createNotification 메서드가 1번 호출되었는지 확인
        verify(notificationService, times(1)).createNotification(notificationDtoList);
    }

    @Test
    @DisplayName("성공 - getTaskType 메서드가 'fetchNotificationData' 반환")
    void succeedsToGetTaskType() {
        // when
        String actualTaskType = notificationDataFetchingTaskHandler.getTaskType();

        // then
        String expectedTaskType = "fetchNotificationData";
        assertThat(actualTaskType).isEqualTo(expectedTaskType);
    }

    @Test
    @DisplayName("성공 - getScheduleIntervalMillis 메서드가 60000L 반환")
    void succeedsToGetScheduleIntervalMillis() {
        // when
        long actualIntervalMillis = notificationDataFetchingTaskHandler.getScheduleIntervalMillis();

        // then
        long expectedIntervalMillis = 60000L;
        assertThat(actualIntervalMillis).isEqualTo(expectedIntervalMillis);
    }
}