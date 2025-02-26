package com.project.cheerha.domain.user.dto.response;

public record VerifyPasswordResetCodeResponseDto(String message, String resetToken) {
    public static VerifyPasswordResetCodeResponseDto of(String resetToken) {
        return new VerifyPasswordResetCodeResponseDto("비밀번호 변경용 인증에 성공하였습니다.", resetToken);
    }
}
