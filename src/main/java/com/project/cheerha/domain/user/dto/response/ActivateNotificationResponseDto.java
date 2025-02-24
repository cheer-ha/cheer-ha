package com.project.cheerha.domain.user.dto.response;

public record ActivateNotificationResponseDto(String message) {
    private static ActivateNotificationResponseDto of(){
        return new ActivateNotificationResponseDto("이메일 알림 수신이 활성화되었습니다.");
    }
}
