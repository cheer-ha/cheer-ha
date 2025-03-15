package com.project.cheerha.domain.notification.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentCaptor.forClass;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.ArgumentMatchers.any;

import com.project.cheerha.domain.notification.dto.NotificationDto;
import com.project.cheerha.domain.notification.entity.Notification;
import com.project.cheerha.domain.notification.repository.NotificationRepository;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {

    @Mock
    private NotificationRepository notificationRepository;

    @InjectMocks
    private NotificationService notificationService;

    private List<NotificationDto> notificationDtoList;

    @BeforeEach
    void setUp() {
        // 테스트에 사용할 데이터 초기화
        notificationDtoList = Arrays.asList(
            new NotificationDto("user1@example.com", "https://job.com/1"),
            new NotificationDto("user1@example.com", "https://job.com/2"),
            new NotificationDto("user2@example.com", "https://job.com/3")
        );
    }

    @Test
    @DisplayName("성공 - 알림 생성")
    void succeedsToCreateNotification() {
        // given
        List<Notification> notificationList = notificationDtoList.stream()
            .map(dto -> Notification.toEntity(
                dto.email(),
                dto.jobOpeningUrl()
            )).toList();

        // when
        // 알림을 생성하는 메서드 실행
        notificationService.createNotification(notificationDtoList);

        // then
        // notificationRepository의 saveAll 메서드가 호출될 때, 저장된 알림 리스트 캡처
        // 목적: '메서드 호출 여부'뿐만 아니라 '그 호출에 전달된 값이 예상대로 맞는지' 검증
        ArgumentCaptor<List<Notification>> captor = forClass(List.class);
        verify(notificationRepository, times(1)).saveAll(captor.capture());

        // 저장된 알림 리스트를 가져와서 원본 notificationList와 비교
        List<Notification> capturedNotificationList = captor.getValue();

        // notificationList와 capturedNotifications의 크기가 같은지 검증
        assertEquals(notificationList.size(), capturedNotificationList.size());

        // 각 Notification 객체의 이메일과 URL이 일치하는지 검증
        for (int i = 0; i < notificationList.size(); i++) {
            assertEquals(notificationList.get(i).getEmail(), capturedNotificationList.get(i).getEmail());
            assertEquals(notificationList.get(i).getJobOpeningUrl(), capturedNotificationList.get(i).getJobOpeningUrl());
        }
    }

    @Test
    @DisplayName("확인 - 빈 리스트 전달 시 saveAll() 메서드가 호출되지 않는지 확인")
    void doesNotSaveEmptyNotificationList() {
        // given
        // 빈 NotificationDto 리스트 준비
        List<NotificationDto> emptyNotificationDtoList = Collections.emptyList();

        // when
        // 빈 리스트로 알림을 생성하는 메서드 실행
        notificationService.createNotification(emptyNotificationDtoList);

        // then
        // saveAll 메서드가 호출되지 않았는지 확인
        verify(notificationRepository, times(0)).saveAll(any(List.class));
    }
}