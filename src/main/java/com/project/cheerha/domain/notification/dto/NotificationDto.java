package com.project.cheerha.domain.notification.dto;

import com.querydsl.core.annotations.QueryProjection;

public record NotificationDto(String email, String jobOpeningUrl) {
    @QueryProjection
    public NotificationDto {}

}
