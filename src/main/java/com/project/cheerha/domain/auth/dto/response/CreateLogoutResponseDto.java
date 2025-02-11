package com.project.cheerha.domain.auth.dto.response;

public record CreateLogoutResponseDto(
    String message
) {
    public static CreateLogoutResponseDto of() {
        return new CreateLogoutResponseDto("로그아웃 성공");
    }
}
