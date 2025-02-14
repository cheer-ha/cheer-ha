package com.project.cheerha.common.exception.auth;

import com.project.cheerha.common.exception.base.BaseException;

public class ForbiddenException extends BaseException {

    public ForbiddenException(AuthErrorCode errorCode) {
        super(errorCode.getMessage(), errorCode.getStatus());
    }
}
