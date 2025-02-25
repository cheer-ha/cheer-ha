package com.project.cheerha.common.exception.client;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ClientErrorCode {

    /**
     *  Exeception Enum의 경우 가독성 향상을 위해 상태코드 기준으로 묶어서 분리되었습니다.
     *  클라이언트의 요청에 따른 예외처리 Enum입니다.
     *  잘못된 요청일 시에 400번(BadRequest)입니다.
     **/

    //BadRequestException
    ALREADY_EXIST_EMAIL(HttpStatus.BAD_REQUEST, "이미 존재하는 이메일입니다."),
    INVALID_ENUM_VALUE(HttpStatus.BAD_REQUEST, "유효하지 않은 값입니다. 허용된 값 중에서 선택해주세요."),
    MIN_AGE_EXCEEDS_MAX_AGE(HttpStatus.BAD_REQUEST, "최소 나이는 최대 나이보다 작거나 같아야 합니다."),
    INVALID_EMAIL_VERIFICATION_CODE(HttpStatus.BAD_REQUEST, "이메일 인증 코드가 올바르지 않습니다.");


    private final HttpStatus status;
    private final String message;
}
