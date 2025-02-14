package com.project.cheerha.common.exception.data;

import com.project.cheerha.common.exception.base.BaseException;

public class NotFoundException extends BaseException {

    public NotFoundException(DataErrorCode errorCode) {
        super(errorCode.getMessage(), errorCode.getStatus());
    }

}