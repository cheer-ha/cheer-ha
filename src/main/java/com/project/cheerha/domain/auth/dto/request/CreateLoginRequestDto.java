package com.project.cheerha.domain.auth.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record CreateLoginRequestDto(
    @Email String email,
    @NotBlank String password
) {

}
