package com.project.cheerha.domain.user.dto.request;

public record UpdatePasswordRequestDto(String oldPassword, String newPassword) {
}
