package com.project.cheerha.domain.auth.dto.response;

public record CreateUserResponseDto(
    String message
) {
    public static CreateUserResponseDto of(String message) {
        return new CreateUserResponseDto(message);
    }
}
