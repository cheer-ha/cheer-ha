package com.project.cheerha.common.exeption;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 사용자입니다."),
    ALREADY_EXIST_EMAIL(HttpStatus.BAD_REQUEST, "이미 존재하는 이메일입니다.");

    private final HttpStatus status;
    private final String message;
}
