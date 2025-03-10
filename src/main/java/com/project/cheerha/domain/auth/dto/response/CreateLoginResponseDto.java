package com.project.cheerha.domain.auth.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)  //refreshToken 반환값이 항상 null 이라 무시
public record CreateLoginResponseDto(
    String message,
    String token,
    String refreshToken
) {
    public static CreateLoginResponseDto toDto(String token, String refreshToken) {
        return new CreateLoginResponseDto("로그인 성공", token, refreshToken);
    }
}
