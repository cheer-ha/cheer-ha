package com.project.cheerha.domain.auth.dto.response;

public record CreateSignupResponseDto(
    String message
) {
    public static CreateSignupResponseDto of() {
        return new CreateSignupResponseDto("회원가입 성공");
    }
}
