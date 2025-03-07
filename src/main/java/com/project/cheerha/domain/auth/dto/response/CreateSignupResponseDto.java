package com.project.cheerha.domain.auth.dto.response;

public record CreateSignupResponseDto(
    String message
) {
    public static CreateSignupResponseDto toDto() {
        return new CreateSignupResponseDto("이메일 인증토큰이 발송되었습니다.");
    }
}
