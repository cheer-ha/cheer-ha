package com.project.cheerha.common.exception.server;

import com.project.cheerha.common.exception.base.BaseException;

public class IllegalStatusException extends BaseException {

    public IllegalStatusException(ServerErrorCode errorCode) {
        super(errorCode.getMessage(), errorCode.getStatus());
    }
}