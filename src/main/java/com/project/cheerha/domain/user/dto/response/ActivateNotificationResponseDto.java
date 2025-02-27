package com.project.cheerha.domain.user.dto.response;

public record ActivateNotificationResponseDto(String message) {
    public static ActivateNotificationResponseDto toDto(){
        return new ActivateNotificationResponseDto("이메일 알림 수신이 활성화되었습니다.");
    }
}
