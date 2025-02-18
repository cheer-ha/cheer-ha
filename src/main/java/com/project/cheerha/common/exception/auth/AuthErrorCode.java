package com.project.cheerha.common.exception.auth;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum AuthErrorCode {

    /**
     *  Exeception Enum의 경우 가독성 향상을 위해 상태코드 기준으로 묶어서 분리되었습니다.
     *  인증 및 보안 관련 예외처리 Enum입니다.
     *  권한이 없을 때 401(UnAuthorized), 존재하지 않는 정보일 때 403번(Forbidden)
     **/

    //ForbiddenException
    LOGIN_REQUIRED(HttpStatus.FORBIDDEN, "권한이 없습니다."),

    //UnAuthorizedException
    TOKEN_UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "존재하지 않는 토큰입니다."),
    BLOCKED_EMAIL(HttpStatus.UNAUTHORIZED, "비밀번호를 5회 이상 틀렸습니다. 15분 뒤 다시 시도해주세요"),
    INVALID_EMAIL(HttpStatus.UNAUTHORIZED, "이메일이 잘못되었습니다."),
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "패스워드가 잘못되었습니다.");

    private final HttpStatus status;
    private final String message;
}
