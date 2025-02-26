package com.project.cheerha.domain.user.dto.request;

public record PasswordResetVerifyEmailCodeRequestDto(String email, String code) {
}
