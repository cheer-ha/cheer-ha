package com.project.cheerha.domain.user.dto.response;

public record ReadUserResponseDto(
    String email,
    String name,
    int career
) {
    public static ReadUserResponseDto of(String email, String name, int career) {
        return new ReadUserResponseDto(email, name, career);
    }
}
