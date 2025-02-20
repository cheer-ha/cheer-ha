package com.project.cheerha.common.exception.data;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum DataErrorCode {

    /**
     *  Exeception Enum의 경우 가독성 향상을 위해 상태코드 기준으로 묶어서 분리되었습니다.
     *  데이터 처리 관련 예외처리 Enum입니다.
     *  해당 데이터가 존재하지 않을 때 404(NotFound) 입니다.
     **/

    //NotFoundException
    JOB_OPENING_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 채용공고입니다."),
    KEYWORD_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 키워드입니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 사용자입니다.");

    private final HttpStatus status;
    private final String message;
}
