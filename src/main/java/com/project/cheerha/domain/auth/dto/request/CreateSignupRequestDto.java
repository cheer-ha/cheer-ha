package com.project.cheerha.domain.auth.dto.request;

import jakarta.validation.constraints.*;

public record CreateSignupRequestDto(
    @Email String email
) {
}
