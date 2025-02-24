package com.project.cheerha.common.exception.server;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ServerErrorCode {

    //IllegalStatusException
    ALREADY_RUNNING_SCHEDULER(HttpStatus.CONFLICT, "다른 인스턴스에서 실행중인 스케줄러입니다.");

    private final HttpStatus status;
    private final String message;
}
