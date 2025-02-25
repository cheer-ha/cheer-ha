package com.project.cheerha.domain.user.dto.request;

import com.project.cheerha.domain.user.dto.Purpose;

public record SendEmailVerificationRequestDto(String email, Purpose purpose) {
}
