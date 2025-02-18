package com.project.cheerha.domain.auth.dto.response;

public record RefreshAccessTokenResponseDto(
        String refreshToken, String message
) {
    public static RefreshAccessTokenResponseDto of(String refreshToken) {
        return new RefreshAccessTokenResponseDto(refreshToken, "AccessToken 재발급 성공");
    }
}
