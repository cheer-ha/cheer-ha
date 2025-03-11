package com.project.cheerha.domain.auth.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record RefreshAccessTokenResponseDto(
        String accessToken, String message, String refreshToken
) {
    public static RefreshAccessTokenResponseDto toDto(String accessToken, String refreshToken) {
        return new RefreshAccessTokenResponseDto(accessToken, "AccessToken 재발급 성공", refreshToken);
    }
}
