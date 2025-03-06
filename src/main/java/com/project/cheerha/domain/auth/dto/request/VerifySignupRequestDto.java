package com.project.cheerha.domain.auth.dto.request;

import jakarta.validation.constraints.*;

public record VerifySignupRequestDto(
    @Email String email,
    //8글자 이상, 영소문자, 특수문자, 숫자 각 1개 이상
    @Pattern(regexp = "^(?=.*[a-z])(?=.*\\d)(?=.*[\\W_]).{8,}$") @NotBlank String password,
    @NotBlank String name,
    @NotNull @Min(20) @Max(80) int age,
    @NotNull @Min(0) @Max(50) int career,
    String token
) {
}
