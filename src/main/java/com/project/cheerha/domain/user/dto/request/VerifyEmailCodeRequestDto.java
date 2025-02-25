package com.project.cheerha.domain.user.dto.request;

import com.project.cheerha.domain.user.dto.Purpose;

public record VerifyEmailCodeRequestDto(String email, String code, Purpose purpose) {
}
