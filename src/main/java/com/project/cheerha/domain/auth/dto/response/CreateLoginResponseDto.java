package com.project.cheerha.domain.auth.dto.response;

public record CreateLoginResponseDto(
    String message,
    String token,
    String refreshToken
) {
    public static CreateLoginResponseDto toDto(String token, String refreshToken) {
        return new CreateLoginResponseDto("로그인 성공", token, refreshToken);
    }
}
