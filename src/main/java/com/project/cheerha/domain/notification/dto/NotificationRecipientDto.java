package com.project.cheerha.domain.notification.dto;

import com.querydsl.core.annotations.QueryProjection;

public record NotificationRecipientDto(
    Long keywordId,
    String email
) {

    @QueryProjection
    public NotificationRecipientDto(
        Long keywordId,
        String email
    ) {
        this.keywordId = keywordId;
        this.email = email;
    }
}