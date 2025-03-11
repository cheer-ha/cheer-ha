package com.project.cheerha.domain.notification.scheduler;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentCaptor.forClass;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.project.cheerha.domain.notification.dto.NotificationRecipientDto;
import com.project.cheerha.domain.notification.repository.NotificationDataProviderQuery;
import com.project.cheerha.domain.notification.service.NotificationService;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class NotificationDataFetchingTaskHandlerTest {

    @Mock
    private NotificationService notificationService;

    @Mock
    private NotificationDataProviderQuery notificationDataProviderQuery;

    @InjectMocks
    private NotificationDataFetchingTaskHandler notificationDataFetchingTaskHandler;

    private final Long keywordOneId = 1L;
    private final Long keywordTwoId = 2L;
    private final List<String> jobOpeningUrlList = Arrays.asList(
        "https://job.com/1",
        "https://job/com/2"
    );

    private final List<NotificationRecipientDto> notificationRecipientDtoList = Arrays.asList(
        new NotificationRecipientDto(keywordOneId, "user1@example.com"),
        new NotificationRecipientDto(keywordTwoId, "user2@example.com"));

    @Test
    @DisplayName("성공 - 주기적으로 알림 생성용 데이터 조회")
    void succeedsToFetchDataForNotificationCreation() {
        // given
        Map<Long, List<String>> keywordIdToUrlList = Map.of(keywordOneId, jobOpeningUrlList);

        // ArgumentCaptor:
        // (1) 메서드 호출 시 전달된 인자를 캡처하여 나중에 추후 검증할 수 있게 해주는 Mockito 도구
        // (2) 여기서는 'ZonedDateTime' 유형 인자를 캡처할 준비를 함
        // 테스트하려는 메서드가 findKeywordIdToUrlList() 호출 시 현재를 기준으로 한 'ZonedDateTime' 값을 인자로 넘기므로, 이 값이 1시간 전인지 확인해야 함
        ArgumentCaptor<ZonedDateTime> captor = forClass(ZonedDateTime.class);

        // when
        // findKeywordIdToUrlList 호출 시 captor로 ZonedDateTime 캡처 후 keywordIdToUrlList 반환
        when(notificationDataProviderQuery.findKeywordIdToUrlList(captor.capture())).thenReturn(keywordIdToUrlList);

        // findNotificationRecipientDtoList 호출 시 수신자 DTO 목록 반환
        when(notificationDataProviderQuery.findNotificationRecipientDtoList()).thenReturn(notificationRecipientDtoList);

        // 실제 테스트 대상인 메서드 호출
        // 테스트에서는 payload 값이 중요하지 않으므로 null 전달
        // handle()을 호출하지 않으면, 메서드 내의 로직이 실행되지 않기 때문에 후속 검증도 불가능함
        notificationDataFetchingTaskHandler.handle(null);

        // then
        // findKeywordIdToUrlList가 1번만 호출되었는지 확인
        verify(notificationDataProviderQuery, times(1)).findKeywordIdToUrlList(captor.capture());

        // captor로 캡처한 ZonedDateTime 값 추출
        // == 'findKeywordIdToUrlList' 호출 시 사용된 시간
        ZonedDateTime capturedReferenceTime = captor.getValue();

        // capturedReferenceTime이 현재 시간 기준 1-2시간 전에 해당하는지 검증
        // 시간차에 따라 테스트가 실패할 가능성을 방지하고자 1-2시간이라는 범위 지정
        assert capturedReferenceTime.isAfter(ZonedDateTime.now().minusHours(2L))  // 2시간 전에 해당하는 시간 이후여야 함
            && capturedReferenceTime.isBefore(ZonedDateTime.now().minusHours(1L));  // 1시간 전에 해당하는 시간 이전이어야 함

        // findNotificationRecipientDtoList 메서드가 1번 호출되었는지 확인
        verify(notificationDataProviderQuery, times(1)).findNotificationRecipientDtoList();

        // createNotification 메서드가 1번 호출되었는지 확인
        verify(notificationService, times(1)).createNotification(notificationRecipientDtoList, keywordIdToUrlList);
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
    @DisplayName("성공 - getScheduleIntervalMillis 메서드가 3600000L 반환")
    void succeedsToGetScheduleIntervalMillis() {
        // when
        long actualIntervalMills = notificationDataFetchingTaskHandler.getScheduleIntervalMillis();

        // then
        long expectedIntervalMills = 3600000L;
        assertThat(actualIntervalMills).isEqualTo(expectedIntervalMills);
    }
}