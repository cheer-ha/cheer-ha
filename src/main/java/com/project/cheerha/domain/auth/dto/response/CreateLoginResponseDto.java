package com.project.cheerha.domain.auth.dto.response;

public record CreateLoginResponseDto(
    String message,
    String token
) {

    public static CreateLoginResponseDto of(String token, String message) {
        return new CreateLoginResponseDto(token, message);
    }
}
