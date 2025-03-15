package com.project.cheerha.domain.notification.repository;

import com.project.cheerha.domain.notification.dto.NotificationDto;
import java.time.ZonedDateTime;
import java.util.List;

public interface NotificationRepositoryQuery {

    List<NotificationDto> findTopMatchingJobOpeningsWithUsers(ZonedDateTime referenceTime);
}