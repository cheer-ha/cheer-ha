package com.project.cheerha.domain.auth.dto.response;

public record RefreshAccessTokenResponseDto(
        String accessToken, String message
) {
    public static RefreshAccessTokenResponseDto of(String accessToken) {
        return new RefreshAccessTokenResponseDto(accessToken, "AccessToken 재발급 성공");
    }
}
