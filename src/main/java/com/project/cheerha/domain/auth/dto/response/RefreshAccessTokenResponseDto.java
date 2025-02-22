package com.project.cheerha.domain.auth.dto.response;

public record RefreshAccessTokenResponseDto(
        String accessToken, String message, String refreshToken
) {
    public static RefreshAccessTokenResponseDto of(String accessToken, String refreshToken) {
        return new RefreshAccessTokenResponseDto(accessToken, "AccessToken 재발급 성공", refreshToken);
    }
}
