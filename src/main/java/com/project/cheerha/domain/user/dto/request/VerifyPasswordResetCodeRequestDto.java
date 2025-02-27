package com.project.cheerha.domain.user.dto.request;

public record VerifyPasswordResetCodeRequestDto(String email, String code) {
}
