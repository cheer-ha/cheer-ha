package com.project.cheerha.domain.auth.dto.response;

public record CreateUserResponseDto(
    String message
) {
    public static CreateUserResponseDto of() {
        return new CreateUserResponseDto("회원가입 성공");
    }
}
