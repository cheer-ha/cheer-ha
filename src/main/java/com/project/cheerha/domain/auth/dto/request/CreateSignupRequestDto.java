package com.project.cheerha.domain.auth.dto.request;

import jakarta.validation.constraints.*;

public record CreateSignupRequestDto(
    @Email String email,
    @NotBlank String password,
    @NotBlank String name,
    @NotNull @Min(20) @Max(80) int age,
    @NotNull @Min(0) @Max(50) int career
) {
}
