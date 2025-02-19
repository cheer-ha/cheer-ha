package com.project.cheerha.domain.user.dto.response;

public record ReadUserResponseDto(
    String email,
    String name,
    int career,
    int age
) {
    public static ReadUserResponseDto of(String email, String name, int career, int age) {
        return new ReadUserResponseDto(email, name, career, age);
    }
}
