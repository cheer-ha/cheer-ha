package com.project.cheerha.domain.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record ResetPasswordRequestDto(
        String email,
        @Pattern(regexp = "^(?=.*[a-z])(?=.*\\d)(?=.*[\\W_]).{8,}$", message = "올바른 비밀번호가 아닙니다.") @NotBlank String newPassword,
        String token
) {
}
