package com.project.cheerha.domain.auth.dto.response;

public record VerifySignupResponseDto(
    String message
) {
    public static VerifySignupResponseDto toDto() {
        return new VerifySignupResponseDto("회원가입 성공");
    }
}
