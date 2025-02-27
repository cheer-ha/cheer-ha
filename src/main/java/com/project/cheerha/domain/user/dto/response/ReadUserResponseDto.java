package com.project.cheerha.domain.user.dto.response;

public record ReadUserResponseDto(
    String email,
    String name,
    int career,
    int age,
    boolean isNotificationEnabled
) {
    public static ReadUserResponseDto toDto(String email, String name, int career, int age, boolean isNotificationEnabled) {
        return new ReadUserResponseDto(email, name, career, age, isNotificationEnabled);
    }
}
