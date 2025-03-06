package com.project.cheerha.domain.auth.dto.request;

import jakarta.validation.constraints.*;

public record VerifySignupRequestDto(
    @Email String email,
    @Pattern(regexp = "^(?=.*[a-z])(?=.*\\d)(?=.*[\\W_]).{8,}$", message = "올바른 비밀번호가 아닙니다.") @NotBlank String password,
    @NotBlank String name,
    @NotNull @Min(20) @Max(80) int age,
    @NotNull @Min(0) @Max(50) int career,
    String token
) {
}
