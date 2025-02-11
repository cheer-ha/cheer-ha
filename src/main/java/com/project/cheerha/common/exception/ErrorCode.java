package com.project.cheerha.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    ALREADY_EXIST_EMAIL(HttpStatus.BAD_REQUEST, "이미 존재하는 이메일입니다."),

    KEYWORD_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 키워드입니다."),

    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 사용자입니다."),

    TOKEN_NOT_FOUND(HttpStatus.UNAUTHORIZED, "존재하지 않는 토큰입니다."),
    WRONG_EMAIL_OR_PASSWORD(HttpStatus.UNAUTHORIZED, "이메일이나 패스워드가 잘못되었습니다.");

    private final HttpStatus status;
    private final String message;
}
