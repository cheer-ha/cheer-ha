package com.project.cheerha.domain.user.dto.response;

public record CreatePasswordResetTokenResponseDto(String message, String resetToken) {
    public static CreatePasswordResetTokenResponseDto of(String resetToken) {
        return new CreatePasswordResetTokenResponseDto("비밀번호 변경용 인증에 성공하였습니다.", resetToken);
    }
}
