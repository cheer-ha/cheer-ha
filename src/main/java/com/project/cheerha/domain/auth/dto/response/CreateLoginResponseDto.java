package com.project.cheerha.domain.auth.dto.response;

public record CreateLoginResponseDto(
    String message,
    String token
) {
    public static CreateLoginResponseDto of(String token) {
        return new CreateLoginResponseDto("로그인 성공", token);
    }
}
