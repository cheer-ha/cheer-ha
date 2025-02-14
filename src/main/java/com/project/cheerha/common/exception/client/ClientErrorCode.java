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
    PAGING_ERROR(HttpStatus.BAD_REQUEST, "페이지 설정이 잘못되었습니다."),
    ALREADY_EXIST_EMAIL(HttpStatus.BAD_REQUEST, "이미 존재하는 이메일입니다.");

    private final HttpStatus status;
    private final String message;
}
