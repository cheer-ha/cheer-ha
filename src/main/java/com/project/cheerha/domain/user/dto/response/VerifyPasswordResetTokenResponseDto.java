package com.project.cheerha.domain.user.dto.response;

public record VerifyPasswordResetTokenResponseDto(String email, String message, String resetToken) {
    public static VerifyPasswordResetTokenResponseDto toDto(String email, String resetToken) {
        return new VerifyPasswordResetTokenResponseDto(email, "비밀번호 변경용 인증에 성공하였습니다.", resetToken);
    }
}
