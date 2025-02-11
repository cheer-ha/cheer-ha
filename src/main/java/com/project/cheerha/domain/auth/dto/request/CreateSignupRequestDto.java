package com.project.cheerha.domain.auth.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateSignupRequestDto(
    @Email String email,
    @NotBlank String password,
    @NotBlank String name,
    @NotNull int age,
    @NotNull int career
) {

}
