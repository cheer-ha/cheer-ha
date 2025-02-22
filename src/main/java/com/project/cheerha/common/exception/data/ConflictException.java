package com.project.cheerha.common.exception.data;

import com.project.cheerha.common.exception.base.BaseException;

public class ConflictException extends BaseException {

    public ConflictException(DataErrorCode errorCode) {
        super(errorCode.getMessage(), errorCode.getStatus());
    }

}