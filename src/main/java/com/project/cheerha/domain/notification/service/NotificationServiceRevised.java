package com.project.cheerha.domain.notification.service;

import com.project.cheerha.domain.notification.dto.NotificationDto;
import com.project.cheerha.domain.notification.entity.Notification;
import com.project.cheerha.domain.notification.repository.NotificationRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationServiceRevised {

    private final NotificationRepository notificationRepository;

    public void createNotification(List<NotificationDto> notificationDtoList) {
        List<Notification> notificationList = notificationDtoList.stream()
            .map(dto -> Notification.toEntity(
                dto.email(),
                dto.jobOpeningUrl()
            )).toList();

        // 한번에 여러 알림을 저장
        notificationRepository.saveAll(notificationList);
    }
}