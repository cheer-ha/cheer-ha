package com.project.cheerha.domain.user.dto.response;

public record UpdatePasswordResponseDto(String message) {
    public static UpdatePasswordResponseDto toDto() {
        return new UpdatePasswordResponseDto("비밀번호가 변경되었습니다.");
    }
}
