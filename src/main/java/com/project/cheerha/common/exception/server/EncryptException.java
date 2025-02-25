package com.project.cheerha.common.exception.server;

import com.project.cheerha.common.exception.base.BaseException;

public class EncryptException extends BaseException {

    public EncryptException(ServerErrorCode errorCode) {
        super(errorCode.getMessage(), errorCode.getStatus());
    }

}