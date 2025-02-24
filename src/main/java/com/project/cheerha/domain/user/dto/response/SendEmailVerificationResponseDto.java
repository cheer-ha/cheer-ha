package com.project.cheerha.domain.user.dto.response;

public record SendEmailVerificationResponseDto(String message) {
    public static SendEmailVerificationResponseDto of() {
        return new SendEmailVerificationResponseDto("이메일 인증 요청이 전송되었습니다.");
    }
}
