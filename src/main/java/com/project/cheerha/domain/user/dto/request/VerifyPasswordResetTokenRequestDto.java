package com.project.cheerha.domain.user.dto.request;

public record VerifyPasswordResetTokenRequestDto(String email, String token) {
}
