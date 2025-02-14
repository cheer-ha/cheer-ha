package com.project.cheerha.common.exception.auth;

import com.project.cheerha.common.exception.base.BaseException;

public class UnAuthorizedException extends BaseException {

    public UnAuthorizedException(AuthErrorCode errorCode) {
        super(errorCode.getMessage(), errorCode.getStatus());
    }

}
