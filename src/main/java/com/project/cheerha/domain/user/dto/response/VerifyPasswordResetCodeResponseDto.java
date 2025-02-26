package com.project.cheerha.domain.user.dto.response;

public record VerifyPasswordResetCodeResponseDto(String email, String message, String resetToken) {
    public static VerifyPasswordResetCodeResponseDto of(String email, String resetToken) {
        return new VerifyPasswordResetCodeResponseDto(email, "비밀번호 변경용 인증에 성공하였습니다.", resetToken);
    }
}
