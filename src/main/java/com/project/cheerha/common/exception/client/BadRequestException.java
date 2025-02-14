package com.project.cheerha.common.exception.client;

import com.project.cheerha.common.exception.base.BaseException;

public class BadRequestException extends BaseException {

    public BadRequestException(ClientErrorCode errorCode) {
        super(errorCode.getMessage(), errorCode.getStatus());
    }
}
