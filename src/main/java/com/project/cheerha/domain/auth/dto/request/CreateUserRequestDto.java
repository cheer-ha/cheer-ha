package com.project.cheerha.domain.auth.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record CreateUserRequestDto(
    @Email String email,
    @NotBlank String password,
    @NotBlank String name,
    @NotBlank int age,
    @NotBlank int career
) {

}
