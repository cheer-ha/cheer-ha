package com.project.cheerha.domain.user.dto.request;

public record ResetPasswordRequestDto(String email, String newPassword, String token) {
}
