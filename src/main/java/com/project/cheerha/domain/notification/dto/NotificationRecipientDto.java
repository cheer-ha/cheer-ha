package com.project.cheerha.domain.notification.dto;

import com.querydsl.core.annotations.QueryProjection;

/**
 * 이메일 알림을 받을 사용자 정보를 담은 DTO
 * @param keywordId: 사용자가 선택한 기술 키워드 ID
 * @param email: 사용자의 이메일
 */
public record NotificationRecipientDto(
    Long keywordId,
    String email
) {
    @QueryProjection
    public NotificationRecipientDto {
    }
}